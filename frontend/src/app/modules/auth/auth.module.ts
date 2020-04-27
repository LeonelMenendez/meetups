import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatModule } from 'src/app/shared/modules/mat/mat.module';

import { AuthRoutingModule } from './auth-routing.module';
import { SignInComponent, SignUpComponent } from './components';

@NgModule({
  declarations: [SignInComponent, SignUpComponent],
  imports: [CommonModule, AuthRoutingModule, ReactiveFormsModule, MatModule],
})
export class AuthModule {}
