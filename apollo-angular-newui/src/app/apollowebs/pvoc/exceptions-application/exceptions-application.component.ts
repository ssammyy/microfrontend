import {Component, OnInit, Output} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-exceptions-application',
  templateUrl: './exceptions-application.component.html',
  styleUrls: ['./exceptions-application.component.css']
})
export class ExceptionsApplicationComponent implements OnInit {
  companyDetails: FormGroup;

  constructor() { }

  ngOnInit(): void {
  }

}
