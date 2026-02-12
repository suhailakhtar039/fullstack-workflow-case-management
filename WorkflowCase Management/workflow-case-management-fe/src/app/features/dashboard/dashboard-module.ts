import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing-module';
import { Dashboard } from './pages/dashboard/dashboard';
import { CaseList } from './pages/case-list/case-list';
import { Inbox } from './pages/inbox/inbox';

// angular material imports
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { FormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { DashboardHome } from './pages/dashboard-home/dashboard-home';

@NgModule({
  declarations: [Dashboard, CaseList, Inbox, DashboardHome],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    FormsModule,
    // angular material imports
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatTableModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    MatButtonToggleModule,
  ],
})
export class DashboardModule {}
