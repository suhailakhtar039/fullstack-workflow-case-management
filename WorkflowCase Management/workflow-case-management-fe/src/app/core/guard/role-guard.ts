import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const expectedRoles = route.data?.['roles'] as string[];

  if (!expectedRoles || expectedRoles.length === 0) return true;

  const userRoles = authService.getRoles();

  const hasAccess = expectedRoles.some((role) => userRoles.includes(role));

  if (!hasAccess) {
    router.navigate(['/forbidden']);
    return false;
  }

  return true;
};
