import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { FormValidatorService } from './services/form-validator.service';

@NgModule({
  declarations: [FormValidatorService],
  imports: [CommonModule],
})
export class CoreModule {}
