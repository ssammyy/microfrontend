import {Component, Inject, OnInit} from '@angular/core';
import {DatePipe} from "@angular/common";
import {interval, Subject} from "rxjs";
import {Store} from "@ngrx/store";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Router} from "@angular/router";
import {LocalDataSource} from "ng2-smart-table";
import {debounce} from "rxjs/operators";
import {selectUserInfo} from "../../../../core/store";
import {MsService} from "../../../../core/store/data/ms/ms.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-epra-list',
  templateUrl: './epra-list.component.html',
  styleUrls: ['./epra-list.component.css']
})
export class EpraListComponent implements OnInit {

  addNewBatchForm!: FormGroup;

  constructor(private formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private dialogRef: MatDialogRef<any>,
              private msService: MsService) {
  }

  ngOnInit(): void {
    this.addNewBatchForm = this.formBuilder.group({
      county: ['', Validators.required],
      town: ['', Validators.required],
      batchFileYear: ['', Validators.required],
      remarks: [''],
    });
  }

}
