import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

import { AuthRoutingModule } from './auth-routing.module';
import { SignInComponent, SignUpComponent } from './components';

@NgModule({
  declarations: [SignInComponent, SignUpComponent],
  imports: [SharedModule, AuthRoutingModule],
})
export class AuthModule {}
