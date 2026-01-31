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

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  login(): void {
    console.log('I have been clicked');
    this.authService.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (err) => console.error(err),
    });
  }
}
