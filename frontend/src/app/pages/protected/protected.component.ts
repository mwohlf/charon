import {Component, OnInit} from '@angular/core';
import {page_spec} from '../page.spec';

@Component({
  selector: 'app-protected',
  templateUrl: './protected.component.html',
  styleUrls: ['./protected.component.scss'],
})
export class ProtectedComponent implements OnInit {

  static SPEC: page_spec = {
    route: 'protected',
    icon: 'loop',
    title: 'Protected',
    component: ProtectedComponent,
  }

  constructor() {
  }

  ngOnInit(): void {
  }

}
