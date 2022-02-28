import { Component, OnInit } from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {FormBuilder, FormGroup, NgForm, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {StandardDevelopmentService} from "../../../../shared/services/standard-development.service";
import {
  Product,
  ProductSubCategory,
  StandardRequest,
  TechnicalCommittee,
  Department
} from "../../../../shared/models/standard-development";

@Component({
  selector: 'app-request-standard-form',
  templateUrl: './request-standard-form.component.html',
  styleUrls: ['./request-standard-form.component.css']
})
export class RequestStandardFormComponent implements OnInit {
  public products !: Product[] ;
  public productsSubCategory !: ProductSubCategory[] ;
  public departments !: Department[] ;
  public committees !: TechnicalCommittee[] ;

  public stdRequestFormGroup!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private standardDevelopmentService:StandardDevelopmentService
  ) { }

  ngOnInit(): void {
    this.getStandards();
    this.getDepartments();
    this.getTechnicalCommittee();

    this.stdRequestFormGroup = this.formBuilder.group({
      name:['', Validators.required],
      phone:['', Validators.required],
      email:['', Validators.required],
      tc_id:['', Validators.required],
      department_id:['', Validators.required],
      productCategory:['', Validators.required],
      product_category:['', Validators.required]
      // postalAddress: ['', [Validators.required, Validators.pattern('P.O.BOX [0-9]{5}')]]
    });
  }

  get formStdRequest(): any{
    return this.stdRequestFormGroup.controls;
  }
  public getStandards(): void{
    this.standardDevelopmentService.getStandards().subscribe(
      (response: Product[])=> {
        this.products = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }
  public getDepartments(): void{
    this.standardDevelopmentService.getDepartments().subscribe(
      (response: Department[])=> {
        this.departments = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

  public getTechnicalCommittee(): void{
    this.standardDevelopmentService.getTechnicalCommittee().subscribe(
      (response: TechnicalCommittee[])=> {
        this.committees = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

   saveStandard(): void{

    this.standardDevelopmentService.addStandardRequest(this.stdRequestFormGroup.value).subscribe(
      (response:StandardRequest) =>{
        console.log(response);
        this.router.navigate(['/login'])
      },
      (error:HttpErrorResponse) =>{
        alert(error.message);
      }
    );
  }

  onSelectProductCategory(value: any): any {
    this.standardDevelopmentService.getProductSubcategory(value).subscribe(
      (response:ProductSubCategory[]) =>{
        console.log(response);
        this.productsSubCategory = response
      },
      (error:HttpErrorResponse) =>{
        alert(error.message);
      }
    );
  }
}
