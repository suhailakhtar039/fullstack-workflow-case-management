import { Component } from '@angular/core';

@Component({
  selector: 'app-dashboard-home',
  standalone: false,
  templateUrl: './dashboard-home.html',
  styleUrl: './dashboard-home.css',
})
export class DashboardHome {
  constructor() {
    console.log('dashboard home is instantiating');
  }
}
