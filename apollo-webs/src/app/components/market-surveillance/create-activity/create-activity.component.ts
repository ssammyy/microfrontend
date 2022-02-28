import {Component, OnInit} from '@angular/core';
import {faArrowLeft} from '@fortawesome/free-solid-svg-icons/faArrowLeft';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {faTrash} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-create-activity',
  templateUrl: './create-activity.component.html',
  styleUrls: ['./create-activity.component.css']
})
export class CreateActivityComponent implements OnInit {

  arrowLeftIcon = faArrowLeft;
  deleteIcon = faTrash;
  public activityFormGroup!: FormGroup;



  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.activityFormGroup = this.formBuilder.group({
      activityType: ['Surveillance', Validators.required],
      activityName: ['', Validators.required],
      department: ['', Validators.required],
      division: ['', Validators.required],
      region: ['', Validators.required],
      county: ['', Validators.required],
      town: ['', Validators.required],
      broadProductCategory: ['', Validators.required],
      productClassification: ['', Validators.required],
      productSubcategory: ['', Validators.required],
      productName: ['', Validators.required],
      resource: [''],
      activityStartDate: [''],
      activityEndDate: [''],
      budget: ['', Validators.required],
    });
  }


}
