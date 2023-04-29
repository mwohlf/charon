import {Component, OnInit, Input } from '@angular/core';
import {MatIconModule} from '@angular/material/icon';
import {ThemePicker} from '../theme-picker/theme-picker';
import {MatButtonModule} from '@angular/material/button';
import {AppThemeModule} from '../../app-theme.module';

@Component({
  imports: [],
  selector: 'app-page',
  standalone: true,
  templateUrl: './page.component.html',
})
export class PageComponent implements OnInit {

  @Input() header: string = '';

  constructor() {
    //
  }

  ngOnInit(): void {
  }

}
