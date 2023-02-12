import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { NgxSpinnerService } from 'ngx-spinner';
import {OverlayService} from "../../../../shared/loader/overlay.service";

@Component({
  selector: 'app-standards-approval-committee',
  templateUrl: './standards-approval-committee.component.html',
  styleUrls: ['./standards-approval-committee.component.css']
})
export class StandardsApprovalCommitteeComponent implements OnInit {

  displayedColumns: string[] = ['slNo','ksNo','title','edition','department', 'date', 'status', 'dateApproved', 'feedback'];

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
      
      date: ['', null],
      department: ['', null],
      status: ['', null],
    });


  }
  applyFilter(formDirective): void {
    this.spinnerService.show("Filtering Data")
  }

}
