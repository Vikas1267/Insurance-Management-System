export class RestApi {
    public readonly API_URL = 'http://localhost:8082';

    public readonly LOGIN_URL = this.API_URL + '/login';
    public readonly POLICY_URL = this.API_URL + '/policy';
    public readonly POLICY_Add_URL = this.POLICY_URL + '/save';
    public readonly POLICY_GET_URL = this.POLICY_URL + '/getAll';
    public readonly POLICY_DELETE_URL = this.POLICY_URL + '/delete';
    public readonly POLICY_PDF_EXPORT_URL = this.POLICY_URL + '/export/pdf';
    public readonly POLICY_CSV_EXPORT_URL = this.POLICY_URL + '/export/csv';
    public readonly POLICY_CALCULATE_PREMIUM_URL = this.POLICY_URL + '/calculate-premium';
    public readonly POLICY_CLAIM_ADJUDICATE_URL = this.POLICY_URL + '/claim/adjudicate';
    public readonly POLICY_DASHBOARD_METRICS_URL = this.POLICY_URL + '/dashboard/metrics';

    public readonly USER_URL = this.API_URL + '/users';
    
    constructor() {

    }

}
