import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CaseService {
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

    const url = myCases
      ? 'http://localhost:8080/api/cases/my'
      : 'http://localhost:8080/api/cases/search';

    return this.http.get<any>(url, { params });
  }
}
