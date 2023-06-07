import { Component, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { NgxSpinnerService } from 'ngx-spinner';
import { OverlayService } from 'src/app/shared/loader/overlay.service';

@Component({
  selector: 'app-qa-sl-reports',
  templateUrl: './qa-sl-reports.component.html',
  styleUrls: ['./qa-sl-reports.component.css']
})
export class QaSlReportsComponent implements OnInit {

  displayedColumns: string[] = ['pin','entryNo','companyName','telephoneNo','emailAddress','physicalLocation','postalAddress','postalCode','town','region','productName','issueDate','expiryDate'];

  filterFormGroup: FormGroup;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
 
  // dataSource!: MatTableDataSource<>;

  constructor(private store$: Store<any>,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private SpinnerService: NgxSpinnerService,
              private spinnerService: OverlayService) {
   }

  ngOnInit(): void {
  }

}
