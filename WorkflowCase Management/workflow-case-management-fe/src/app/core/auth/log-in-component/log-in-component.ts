import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-log-in-component',
  standalone: false,
  templateUrl: './log-in-component.html',
  styleUrl: './log-in-component.css',
})
export class LogInComponent {
  username = '';
  password = '';
  isLoading?: boolean;
  errorMessage?: string;

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  login() {
    this.isLoading = true;
    this.errorMessage = '';

    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        console.log('Navigating to dashboard');
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Invalid username or password';
        this.isLoading = false;
      },
    });
  }
}
