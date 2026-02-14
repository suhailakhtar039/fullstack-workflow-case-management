import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

interface LoginResponse {
  token: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly TOKEN_KEY = 'caseflow_token';
  private readonly API_URL = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.API_URL}/api/auth/login`, { username, password })
      .pipe(tap((res) => this.saveToken(res.token)));
  }

  saveToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getRoles(): string[] {
    const token = this.getToken();
    if (!token) return [];

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const roles = payload.roles || payload.authorities || [];

      return roles
        .filter((r: string) => r.startsWith('ROLE_')) // only real roles
        .map((r: string) => r.replace('ROLE_', '')); // normalize
    } catch {
      return [];
    }
  }

  hasRole(role: string): boolean {
    return this.getRoles().includes(role);
  }

  hasAnyRole(roles: string[]): boolean {
    if (!roles || roles.length === 0) return false;

    const userRoles = this.getRoles();
    if (!userRoles || userRoles.length === 0) return false;

    return roles.some((role) => userRoles.includes(role));
  }
}
