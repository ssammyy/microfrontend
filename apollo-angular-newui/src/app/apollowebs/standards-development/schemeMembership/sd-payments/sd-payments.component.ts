import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";


@Component({
  selector: 'app-sd-payments',
  templateUrl: './sd-payments.component.html',
  styleUrls: ['./sd-payments.component.css']
})
export class SdPaymentsComponent implements OnInit {

  dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();

    loadingText = null

  constructor(
                private formBuilder: FormBuilder,) {
    }


  ngOnInit(): void {
  }

}
