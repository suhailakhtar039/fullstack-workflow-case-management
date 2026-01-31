import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { LogInComponent } from './core/auth/log-in-component/log-in-component';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/interceptors/auth-interceptor';

@NgModule({
  declarations: [App, LogInComponent],
  imports: [BrowserModule, AppRoutingModule, MatIconModule, FormsModule],
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideHttpClient(withInterceptors([authInterceptor])),
  ],
  bootstrap: [App],
})
export class AppModule {}
