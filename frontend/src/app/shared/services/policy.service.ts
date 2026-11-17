import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/observable';
import { ToasterService } from './toaster.service';
import { RestApi } from '../api/rest-api';
import { Policy } from '../models/policy';

@Injectable()
export class PolicyService {

  api = new RestApi();

  dataChange: BehaviorSubject<Policy[]> = new BehaviorSubject<Policy[]>([]);
  dialogData: any;

  constructor (private httpClient: HttpClient, private toasterService:ToasterService) {}

  get data(): Policy[] {
    return this.dataChange.value;
  }

  getDialogData() {
    return this.dialogData;
  }

 getAllPolicy(): void {
  this.httpClient.get<Policy[]>(this.api.POLICY_GET_URL).subscribe(data => {
      this.dataChange.next(data);
    },
    (error: HttpErrorResponse) => {
    console.log (error.name + ' ' + error.message);
    });
}

addPolicy(policy: Policy): void {
  this.httpClient.post(this.api.POLICY_Add_URL, policy).subscribe(data => {
    console.log("response is "+data);
    this.dialogData = data;
    console.log(this.dialogData);
    if(policy.id!=null && policy.id>0){
      this.toasterService.openSuccessSnackBar('Successfully updated','', 2000);
    }else{
      this.toasterService.openSuccessSnackBar('Successfully added','', 2000);
    }
  },
    (err: HttpErrorResponse) => {
      this.toasterService.openErrorSnackBar('Error occurred. Details: ' + err.name + ' ' + err.message,'', 8000);
    });
}

deletePolicy(id: number): void {
  console.log("policy"+id);
  const url = `${this.api.POLICY_DELETE_URL}/${id}`;
  console.log("url=="+url);
  this.httpClient.delete(url).subscribe(data => {
      this.toasterService.openSuccessSnackBar('Successfully deleted','ok', 1000);
    },
    (err: HttpErrorResponse) => {
      this.toasterService.openErrorSnackBar('Error occurred. Details: ' + err.name + ' ' + err.message,'', 8000);
    }
  );
}

getPolicy(id:number):Observable<Policy>{
  console.log("policy"+id);
  const url = `${this.api.POLICY_URL}/${id}`;
  return this.httpClient.get<Policy>(url);
}

exportPdfUrl(id: number): string {
  return `${this.api.POLICY_PDF_EXPORT_URL}/${id}`;
}

exportCsvUrl(): string {
  return this.api.POLICY_CSV_EXPORT_URL;
}

calculatePremium(payload: any): Observable<any> {
  return this.httpClient.post<any>(this.api.POLICY_CALCULATE_PREMIUM_URL, payload);
}

adjudicateClaim(payload: any): Observable<Policy> {
  return this.httpClient.post<Policy>(this.api.POLICY_CLAIM_ADJUDICATE_URL, payload);
}

getDashboardMetrics(): Observable<any> {
  return this.httpClient.get<any>(this.api.POLICY_DASHBOARD_METRICS_URL);
}


}
