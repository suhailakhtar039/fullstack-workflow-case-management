import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Dashboard } from './pages/dashboard/dashboard';
import { CaseList } from './pages/case-list/case-list';
import { Inbox } from './pages/inbox/inbox';
import { roleGuard } from '../../core/guard/role-guard';
import { DashboardHome } from './pages/dashboard-home/dashboard-home';

const routes: Routes = [
  {
    path: '',
    component: Dashboard,
    children: [
      {
        path: '',
        component: DashboardHome,
        pathMatch: 'full',
      },
      {
        path: 'cases',
        component: CaseList,
        canActivate: [roleGuard],
        data: { roles: ['MANAGER', 'ADMIN'] },
      },
      {
        path: 'inbox',
        component: Inbox,
        canActivate: [roleGuard],
        data: { roles: ['REVIEWER', 'APPROVER', 'ADMIN'] },
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DashboardRoutingModule {}
