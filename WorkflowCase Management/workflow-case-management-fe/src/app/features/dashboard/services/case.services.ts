import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CaseService {
  private readonly baseUrl = 'http://localhost:8080/api/cases/search';

  constructor(private http: HttpClient) {}

  getCases(page: number, size: number, search?: string) {
    let params = new HttpParams().set('page', page).set('size', size);

    if (search) {
      params = params.set('title', search);
    }

    return this.http.get<any>(this.baseUrl, { params });
  }
}
