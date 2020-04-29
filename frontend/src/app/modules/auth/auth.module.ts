import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

import { AuthRoutingModule } from './auth-routing.module';
import { AuthComponent } from './auth.component';
import { SignInComponent, SignUpComponent } from './components';

@NgModule({
  declarations: [SignInComponent, SignUpComponent, AuthComponent],
  imports: [SharedModule, AuthRoutingModule],
})
export class AuthModule {}
