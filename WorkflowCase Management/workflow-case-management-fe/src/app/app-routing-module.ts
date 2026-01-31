import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LogInComponent } from './core/auth/log-in-component/log-in-component';
import { authGuardGuard } from './core/guard/auth-guard-guard';

const routes: Routes = [
  { path: 'login', component: LogInComponent },
  {
    path: 'dashboard',
    canActivate: [authGuardGuard],
    loadChildren: () =>
      import('./features/dashboard/dashboard-module').then((m) => m.DashboardModule),
  },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
