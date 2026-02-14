import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forbidden',
  standalone: false,
  templateUrl: './forbidden.html',
  styleUrl: './forbidden.css',
})
export class Forbidden {
  constructor(private router: Router) {}

  goBack() {
    this.router.navigate(['/dashboard']);
  }
}
