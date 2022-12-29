import {Component, OnInit} from '@angular/core';
import {MatIconModule} from '@angular/material/icon';
import {ThemePicker} from '../theme-picker/theme-picker';
import {MatButtonModule} from '@angular/material/button';
import {AppThemeModule} from '../../app-theme.module';

@Component({
  imports: [
    MatIconModule,
    ThemePicker,
    MatButtonModule,
    AppThemeModule,
  ],
  selector: 'app-footer',
  standalone: true,
  templateUrl: './footer.component.html',
})
export class FooterComponent implements OnInit {

  constructor() {
    //
  }

  ngOnInit(): void {
  }

}
