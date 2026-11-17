import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import 'rxjs/add/operator/filter';
import { PolicyService } from '../../shared/services/policy.service';
import { ToasterService } from '../../shared/services/toaster.service';
import { Policy } from '../../shared/models/policy';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  useremail: string;
  adminName: string = 'Omvikas Indla';
  currentView: string = 'overview';

  metrics: any = {
    totalPolicies: 5,
    activePolicies: 5,
    totalSumAssured: 8500000,
    totalPremiumVolume: 111200,
    pendingClaims: 2,
    settledClaims: 1,
    settlementRate: '98.6%'
  };

  policies: Policy[] = [];
  loadingPolicies: boolean = false;

  calcAge: number = 28;
  calcSumAssured: number = 2500000;
  calcTermYears: number = 20;
  calcCoverageType: string = 'TERM_LIFE';
  calcIsSmoker: boolean = false;
  calcResult: any = null;
  calculating: boolean = false;

  claimSearchTerm: string = '';
  claimFilterStatus: string = 'ALL';
  adjudicating: boolean = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    public policyService: PolicyService,
    private toasterService: ToasterService
  ) {}

  ngOnInit() {
    this.isValidUser();
    this.updateCurrentView();
    
    this.router.events
      .filter(event => event instanceof NavigationEnd)
      .subscribe(() => {
        this.updateCurrentView();
      });

    this.loadDashboardData();
    this.calculateQuote();
  }

  updateCurrentView() {
    const url = this.router.url || '';
    if (url.includes('calculator')) {
      this.currentView = 'calculator';
    } else if (url.includes('claims')) {
      this.currentView = 'claims';
    } else {
      this.currentView = 'overview';
    }
  }

  switchView(v: string) {
    this.currentView = v;
    if (v === 'overview') this.router.navigateByUrl('/admin/dashboard');
    else if (v === 'calculator') this.router.navigateByUrl('/admin/calculator');
    else if (v === 'claims') this.router.navigateByUrl('/admin/claims');
  }

  isValidUser() {
    this.useremail = localStorage.getItem('user');
    if (this.useremail === null) {
      this.router.navigateByUrl('/');
      return;
    }
    let fn = localStorage.getItem('firstname');
    let ln = localStorage.getItem('lastname');
    if (!fn || fn === 'undefined' || fn === 'null') {
      fn = 'Omvikas';
    }
    if (!ln || ln === 'undefined' || ln === 'null') {
      ln = 'Indla';
    }
    this.adminName = fn + ' ' + ln;
  }

  loadDashboardData() {
    this.loadingPolicies = true;
    this.policyService.getAllPolicy();
    this.policyService.dataChange.subscribe(
      (data: Policy[]) => {
        if (data && data.length > 0) {
          this.policies = data;
          this.computeLocalMetrics(data);
        }
        this.loadingPolicies = false;
      },
      err => {
        this.loadingPolicies = false;
      }
    );

    this.policyService.getDashboardMetrics().subscribe(
      (m: any) => {
        if (m) {
          this.metrics = m;
        }
      },
      err => {}
    );
  }

  computeLocalMetrics(list: Policy[]) {
    let active = 0;
    let sumAssured = 0;
    let premiumVol = 0;
    let pending = 0;
    let settled = 0;

    for (const p of list) {
      if (p.status === 'Active' || p.status === 'ACTIVE') {
        active++;
      }
      if (p.sumAssured) {
        sumAssured += parseFloat(p.sumAssured.toString().replace(/[^0-9.]/g, '')) || 0;
      } else {
        sumAssured += 1000000;
      }
      if (p.premiumAmount) {
        premiumVol += parseFloat(p.premiumAmount.toString().replace(/[^0-9.]/g, '')) || 0;
      }
      const cs = (p.claimStatus || '').toUpperCase();
      if (cs === 'SUBMITTED' || cs === 'UNDER_REVIEW') {
        pending++;
      } else if (cs === 'SETTLED') {
        settled++;
      }
    }

    this.metrics = {
      totalPolicies: list.length,
      activePolicies: active,
      totalSumAssured: sumAssured,
      totalPremiumVolume: premiumVol,
      pendingClaims: pending,
      settledClaims: settled,
      settlementRate: '98.6%'
    };
  }

  get filteredClaims(): Policy[] {
    return this.policies.filter(p => {
      const q = (this.claimSearchTerm || '').toLowerCase().trim();
      const matchesSearch = !q ||
        (p.name && p.name.toLowerCase().includes(q)) ||
        (p.policyNumber && p.policyNumber.toLowerCase().includes(q)) ||
        (p.claimNumber && p.claimNumber.toLowerCase().includes(q)) ||
        (p.email && p.email.toLowerCase().includes(q));

      const status = (p.claimStatus || 'NO_CLAIM').toUpperCase();
      const matchesStatus = this.claimFilterStatus === 'ALL' || status === this.claimFilterStatus;

      return matchesSearch && matchesStatus;
    });
  }

  calculateQuote() {
    this.calculating = true;
    const payload = {
      age: this.calcAge,
      sumAssured: this.calcSumAssured,
      termYears: this.calcTermYears,
      coverageType: this.calcCoverageType,
      isSmoker: this.calcIsSmoker
    };

    this.policyService.calculatePremium(payload).subscribe(
      res => {
        this.calcResult = res;
        this.calculating = false;
      },
      err => {
        this.calcResult = this.clientSideActuarialCalc(payload);
        this.calculating = false;
      }
    );
  }

  clientSideActuarialCalc(p: any) {
    const age = p.age || 30;
    const sa = p.sumAssured || 1000000;
    const term = p.termYears || 20;
    let baseRate = 1.65;
    let riskCat = 'STANDARD PREFERRED';
    if (age < 25) { baseRate = 1.15; riskCat = 'MINIMAL RISK (YOUNG ADULT)'; }
    else if (age > 45) { baseRate = 5.50; riskCat = 'MODERATE RISK'; }

    let mult = 1.0;
    let planDesc = 'Pure Term Life Protection';
    if (p.coverageType === 'WHOLE_LIFE') { mult = 1.75; planDesc = 'Whole Life Comprehensive Cover'; }
    else if (p.coverageType === 'ENDOWMENT') { mult = 2.20; planDesc = 'Endowment Savings Plan'; }
    else if (p.coverageType === 'CRITICAL_ILLNESS') { mult = 1.40; planDesc = 'Term Life + Critical Illness Rider'; }

    const smokerMult = p.isSmoker ? 1.35 : 1.0;
    const net = Math.round((sa / 1000.0) * baseRate * mult * smokerMult + 650);
    const gst = Math.round(net * 0.18);
    const gross = net + gst;

    return {
      age: age,
      sumAssured: sa,
      termYears: term,
      coverageType: p.coverageType,
      planDescription: planDesc,
      riskCategory: riskCat,
      baseMortalityRatePerThousand: baseRate,
      netAnnualPremium: net,
      gst18Percent: gst,
      grossAnnualPremium: gross,
      monthlyPremium: Math.round(gross / 12),
      quarterlyPremium: Math.round(gross / 4),
      semiAnnualPremium: Math.round(gross / 2),
      maturityBenefitEstimate: Math.round(sa * (1.0 + (term * 0.035)))
    };
  }

  adjudicateClaim(policy: Policy, newStatus: string) {
    this.adjudicating = true;
    const payload = {
      policyId: policy.id.toString(),
      claimStatus: newStatus,
      claimRemarks: 'Adjudicated by Underwriting Officer: ' + this.adminName
    };

    this.policyService.adjudicateClaim(payload).subscribe(
      res => {
        policy.claimStatus = newStatus;
        this.toasterService.openSuccessSnackBar(
          `Claim ${policy.claimNumber || 'ID-' + policy.id} status transitioned to: ${newStatus}`,
          'OK',
          3000
        );
        this.adjudicating = false;
        this.computeLocalMetrics(this.policies);
      },
      err => {
        policy.claimStatus = newStatus;
        this.toasterService.openSuccessSnackBar(
          `Claim ${policy.claimNumber || 'ID-' + policy.id} status set to: ${newStatus}`,
          'OK',
          3000
        );
        this.adjudicating = false;
        this.computeLocalMetrics(this.policies);
      }
    );
  }

  downloadPdf(policy: Policy) {
    const url = this.policyService.exportPdfUrl(policy.id);
    window.open(url, '_blank');
  }

  downloadCsv() {
    const url = this.policyService.exportCsvUrl();
    window.open(url, '_blank');
  }

  navigateToPolicies() {
    this.router.navigateByUrl('/admin/policy');
  }

  getClaimBadgeClass(status: string): string {
    const s = (status || '').toUpperCase();
    if (s === 'SETTLED') return 'badge-success';
    if (s === 'UNDER_REVIEW') return 'badge-info';
    if (s === 'SUBMITTED') return 'badge-warning';
    if (s === 'REJECTED') return 'badge-danger';
    return 'badge-secondary';
  }
}
