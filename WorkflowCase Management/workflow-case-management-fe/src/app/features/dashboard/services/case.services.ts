import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CaseService {
  baseApiUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getCases(page: number, size: number, search?: string, status?: string, myCases?: boolean) {
    let params: any = { page, size };

    if (search && search.trim()) {
      params.caseNumber = search;
      params.title = search;
    }

    if (status) {
      params.status = status;
    }

    const url = myCases ? this.baseApiUrl + '/cases/my' : this.baseApiUrl + '/cases/search';

    return this.http.get<any>(url, { params });
  }

  transitionCase(caseId: number, status: string) {
    return this.http.patch<void>(environment.apiBaseUrl + '/cases/' + caseId + 'status', null, {
      params: { status },
    });
  }
}
