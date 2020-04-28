import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { NgxPermissionsModule } from 'ngx-permissions';
import { CoreModule } from 'src/app/core/core.module';
import { AuthInterceptor } from 'src/app/core/interceptors/auth.interceptor';
import { ErrorInterceptor } from 'src/app/core/interceptors/error.interceptor';
import { JwtInterceptor } from 'src/app/core/interceptors/jwt.interceptor';
import { SharedModule } from 'src/app/shared/shared.module';

import { HomeComponent, InvitationsComponent } from './components';
import { AdminHomeComponent, UserHomeComponent } from './components/home';
import { MenuRoutingModule } from './menu-routing.module';
import { MenuComponent } from './menu.component';

@NgModule({
  declarations: [
    MenuComponent,
    HomeComponent,
    InvitationsComponent,
    UserHomeComponent,
    AdminHomeComponent,
  ],
  imports: [CoreModule, SharedModule, MenuRoutingModule, NgxPermissionsModule.forChild()],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
  ],
})
export class MenuModule {}
