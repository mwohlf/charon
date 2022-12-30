import {Component, OnInit} from '@angular/core';
import {page_spec} from '../page.spec';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit {

  static SPEC: page_spec = {
    route: 'main',
    icon: 'stars',
    title: 'Main',
    component: MainComponent,
  }

  constructor() {
  }

  ngOnInit(): void {
  }

}
