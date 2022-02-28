import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {faArrowLeft, faEdit, faTrash} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  isLinear = false;
  page = 1;
  backIcon = faArrowLeft;
  editIcon = faEdit;
  deleteIcon = faTrash;

  constructor(private formBuilder: FormBuilder) {
  }

  public firstFormGroup!: FormGroup;
  public secondFormGroup!: FormGroup;
  public thirdFormGroup!: FormGroup;

  ngOnInit(): void {
    this.firstFormGroup = this.formBuilder.group({
      businessName: ['', Validators.required],
      kraPin: ['', Validators.required],
      lineOfBusiness: ['Manufacturing'],
      natureOfBusiness: ['Clothing'],
      regNumber: ['', Validators.required]
    });
    this.secondFormGroup = this.formBuilder.group({
      email: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      building: ['', Validators.required],
      buildingNumber: [''],
      plotNumber: ['', Validators.required],
      street: ['', Validators.required],
      nearestRoad: ['', Validators.required],
      nearestLandmark: ['', Validators.required],
      contactPerson: ['', Validators.required]
    });
    this.thirdFormGroup = this.formBuilder.group({
      buildingName: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      location: ['', Validators.required],
      street: [''],
      county: ['Nairobi'],
      town: ['', Validators.required],
      postalAddress: ['', Validators.required],
      contactPerson: ['', Validators.required]
    });


  }

  get formFirstGroupForm(): any{
    return this.firstFormGroup.controls;
  }

  onClickNext(valid: boolean): void {
    // console.log('hI');
    if (valid) {
      this.page += 1;
    }
    console.log(this.page);
  }
}
