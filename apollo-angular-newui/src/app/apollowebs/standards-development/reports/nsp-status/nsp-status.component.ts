import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {DataTableDirective} from 'angular-datatables';
import { NgxSpinnerService } from 'ngx-spinner';
import {Store} from '@ngrx/store';
import {OverlayService} from "../../../../shared/loader/overlay.service";

@Component({
  selector: 'app-nsp-status',
  templateUrl: './nsp-status.component.html',
  styleUrls: ['./nsp-status.component.css']
})
export class NspStatusComponent implements OnInit {

  displayedColumns: string[] = ['projectId', 'proposedItem', 'recommendation', 'responsibility', 'sector', 'correspondingInternationalStandard', 'taYear', 'implementationStatus','officerInCharge', 'nspStatus', 'comments'];

  searchFormGroup!: FormGroup;

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
      assignInCharge: ['', null],
      sector: ['', null],
    });


  }
  applyFilter(formDirective): void {
    this.spinnerService.show("Filtering Data")
  }

}
