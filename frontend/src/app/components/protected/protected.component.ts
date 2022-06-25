import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-protected',
  templateUrl: './protected.component.html',
  styleUrls: ['./protected.component.scss']
})
export class ProtectedComponent implements OnInit {

  static ROUTER_PATH: string = "protected";

  constructor() { }

  ngOnInit(): void {
  }

}
