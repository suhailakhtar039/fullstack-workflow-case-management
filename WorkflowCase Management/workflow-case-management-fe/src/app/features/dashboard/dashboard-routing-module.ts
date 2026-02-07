import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Dashboard } from './pages/dashboard/dashboard';
import { CaseList } from './pages/case-list/case-list';

const routes: Routes = [
  {
    path: '',
    component: Dashboard,
    children: [
      { path: 'cases', component: CaseList },
      { path: '', redirectTo: 'cases', pathMatch: 'full' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DashboardRoutingModule {}
