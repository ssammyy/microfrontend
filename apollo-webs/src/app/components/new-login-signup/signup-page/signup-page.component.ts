import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-signup-page',
  templateUrl: './signup-page.component.html',
  styleUrls: ['./signup-page.component.css']
})
export class SignupPageComponent implements OnInit {

  page = 1;

  constructor(private formBuilder: FormBuilder) { }

  public firstFormGroup!: FormGroup;
  public secondFormGroup!: FormGroup;
  public thirdFormGroup!: FormGroup;

  ngOnInit(): void {
  }

  onClickNext(valid: boolean): void {
    // console.log('hI');
    if (valid) {
      this.page += 1;
    }
    console.log(this.page);
  }

}
