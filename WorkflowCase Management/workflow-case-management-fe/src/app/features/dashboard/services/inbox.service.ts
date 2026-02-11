import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class InboxService {
  baseUrl = environment.apiBaseUrl + '/inbox';
  constructor(private http: HttpClient) {}

  getMyPendingTasks() {
    return this.http.get<any[]>(`${this.baseUrl}/my`);
  }
}
