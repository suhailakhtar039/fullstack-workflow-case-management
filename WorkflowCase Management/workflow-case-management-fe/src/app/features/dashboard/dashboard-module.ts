import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing-module';
import { Dashboard } from './pages/dashboard/dashboard';
import { CaseList } from './pages/case-list/case-list';


@NgModule({
  declarations: [
    Dashboard,
    CaseList
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule
  ]
})
export class DashboardModule { }
