import {Component, OnInit} from '@angular/core';

import {Router} from '@angular/router';
import {NewWorkItemService} from "../new-work-item.service";
import {New_work_item, TaskData} from "../../../../shared/models/committee_module";
import {FormBuilder, FormGroup, NgForm, Validators} from '@angular/forms';
import {HttpErrorResponse} from "@angular/common/http";
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-prepare-nwi',
  templateUrl: './prepare-nwi.component.html',
  styleUrls: ['./prepare-nwi.component.css']
})
export class PrepareNwiComponent implements OnInit {

  public nwi: New_work_item[] | undefined;
  submitted = false;
  model: NgbDateStruct | undefined;


  constructor(private nwi_service: NewWorkItemService,
              private router: Router, private formBuilder: FormBuilder) {
  }

  public prepareNWIFormGroup!: FormGroup;

  ngOnInit(): void {
    this.prepareNWIFormGroup = this.formBuilder.group({
      slNo: [''],
      reference: [''],
      ta: [''],
      ed: [''],
      title: [''],
      stage_date: [''],
    });
  }

  // newnwi(): void {
  //   this.submitted = false;
  //   this.nwi = response;
  // }

  save(): void {
    this.nwi_service
      .createNWI(this.prepareNWIFormGroup.value).subscribe(
      (response: TaskData) => {
        console.log(response);
        this.gotoList();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  onSubmit() {
    this.submitted = true;
    this.save();
  }

  gotoList() {
    this.router.navigate(['/nwi']);
  }
}
