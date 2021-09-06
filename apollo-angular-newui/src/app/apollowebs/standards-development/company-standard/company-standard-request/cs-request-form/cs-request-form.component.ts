import { Component, OnInit } from '@angular/core';
import {
  CompanyStandardRequest,
  Department,
  Product,
  ProductSubCategory,
  TechnicalCommittee
} from "../../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {StdComStandardService} from "../../../../../core/store/data/std/std-com-standard.service";
import Swal from "sweetalert2";
import {HttpErrorResponse} from "@angular/common/http";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../../core/store/data/std/notification.service";

@Component({
  selector: 'app-cs-request-form',
  templateUrl: './cs-request-form.component.html',
  styleUrls: ['./cs-request-form.component.css']
})
export class CsRequestFormComponent implements OnInit {
  public products !: Product[] ;
  public productsSubCategory !: ProductSubCategory[] ;
  public departments !: Department[] ;
  public technicalCommittees !: TechnicalCommittee[];
  public stdRequestFormGroup!: FormGroup;
  constructor(
      private formBuilder: FormBuilder,
      private route: ActivatedRoute,
      private router: Router,
      private stdComStandardService: StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService: NotificationService
  ) { }

  ngOnInit(): void {
      this.getDepartments();

    this.stdRequestFormGroup = this.formBuilder.group({
      companyName:['', Validators.required],
      companyPhone:['', Validators.required],
      departmentId:['', Validators.required],
      tcId: ['', Validators.required],
      productId: ['', Validators.required],
      productSubCategoryId:['', Validators.required],
      companyEmail:['', Validators.required]
    });
  }
  get formStdRequest(): any{
    return this.stdRequestFormGroup.controls;
  }

    public getStandards(): void {
        this.stdComStandardService.getStandards().subscribe(
            (response: Product[]) => {
                this.products = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public getDepartments(): void {
        this.stdComStandardService.getDepartments().subscribe(
            (response: Department[]) => {
                this.departments = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    saveStandard(): void {
    this.SpinnerService.show();
    this.stdComStandardService.addStandardRequest(this.stdRequestFormGroup.value).subscribe(
        (response:CompanyStandardRequest) =>{

          this.SpinnerService.hide();
          console.log(response);
          Swal.fire('Thank you...', 'Standard Request Submitted!', 'success').then(r => this.stdRequestFormGroup.reset());


        },
        (error:HttpErrorResponse) =>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  onSelectProductCategory(value: any): any {
    this.stdComStandardService.getProductSubcategory(value).subscribe(
        (response:ProductSubCategory[]) =>{
          console.log(response);
          this.productsSubCategory = response
        },
        (error:HttpErrorResponse) =>{
          alert(error.message);
        }
    );
  }

  onSelectDepartment(value: any): any {
    this.stdComStandardService.getTechnicalCommittee(value).subscribe(
        (response: TechnicalCommittee[]) => {
          console.log(response);
          this.technicalCommittees = response
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }


  onSelectTechnicalCommittee(value: any): any {
    this.stdComStandardService.getProducts(value).subscribe(
        (response: Product[]) => {
          console.log(response);
          this.products = response
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }

}
