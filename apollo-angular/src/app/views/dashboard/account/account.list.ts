import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {Company} from "../../../core/store";

@Component({
  selector: 'app-account',
  templateUrl: './account.list.html',
  styles: []
})
export class AccountList implements OnInit {
  companies?: Observable<Company[]>;

  constructor() {
  }

  ngOnInit(): void {
  }

}
