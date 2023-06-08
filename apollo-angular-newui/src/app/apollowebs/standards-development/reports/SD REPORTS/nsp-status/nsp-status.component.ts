import { Component, OnInit, ViewChild } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {DataTableDirective} from 'angular-datatables';
import { NgxSpinnerService } from 'ngx-spinner';
import {Store} from '@ngrx/store';
import { OverlayService } from 'src/app/shared/loader/overlay.service';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
@Component({
  selector: 'app-nsp-status',
  templateUrl: './nsp-status.component.html',
  styleUrls: ['./nsp-status.component.css']
})
export class NspStatusComponent implements OnInit {

  displayedColumns: string[] = ['projectId', 'proposedItem', 'recommendation', 'responsibility', 'sector', 'correspondingInternationalStandard', 'taYear', 'implementationStatus','officerInCharge', 'nspStatus', 'comments'];

  searchFormGroup!: FormGroup;
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

    this.searchFormGroup = this.formBuilder.group({
      
      correspondingInternationalStandard: ['', null],
      officerInCharge: ['', null],
      sector: ['', null],
    });


  }
  applyFilter(formDirective): void {
    this.spinnerService.show("Filtering Data")
  }

}
