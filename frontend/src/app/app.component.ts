import { Component } from '@angular/core';
import { NgxPermissionsService } from 'ngx-permissions';

import { AuthService } from './core/services/auth.service';
import { Role } from './shared/enums/role';
import { IUser } from './shared/models/user';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  currentUser: IUser;

  constructor(private authService: AuthService, private permissionsService: NgxPermissionsService) {
    this.authService.currentUser.subscribe((currentUser) => {
      this.currentUser = currentUser;
      this.setPermissions();
    });
  }

  private setPermissions() {
    if (!this.currentUser) {
      return this.permissionsService.flushPermissions();
    }

    const role = Role[this.currentUser.role];
    this.permissionsService.loadPermissions(Array.of(role));
  }
}
