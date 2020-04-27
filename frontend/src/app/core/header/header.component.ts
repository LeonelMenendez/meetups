import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  toggleNavbar = true;

  constructor(private router: Router, private authService: AuthService) {}

  signOut() {
    this.authService.signOut();
    this.router.navigate(['/sign-in']);
  }
}
