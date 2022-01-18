import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StepperDataService} from "../../../../core/services/data/stepper-data.service";

@Component({
    selector: 'app-manufacturer-details',
    templateUrl: './manufacturer-details.component.html',
    styleUrls: ['./manufacturer-details.component.css']
})
export class ManufacturerDetailsComponent implements OnInit {
    @Input() companyDetails: any;
    @Input() nextClicked: EventEmitter<any>;
    companyDetailsForm: FormGroup

    constructor(private router: Router, private fb: FormBuilder, private data: StepperDataService) {

    }

    ngOnInit(): void {
        this.companyDetailsForm = this.fb.group({
            companyName: [this.companyDetails ? this.companyDetails.companyName : '', Validators.required],
            email: [this.companyDetails ? this.companyDetails.email : '', Validators.required],
            companyPinNo: [this.companyDetails ? this.companyDetails.companyPinNo : '', Validators.required],
            telephoneNo: [this.companyDetails ? this.companyDetails.telephoneNo : '', Validators.required],
            postalAddress: [this.companyDetails ? this.companyDetails.postalAddress : '', Validators.required],
            physicalLocation: [this.companyDetails ? this.companyDetails.physicalAddress : '', Validators.required],
            contactPersonName: [this.companyDetails ? this.companyDetails.contactPerson : '', Validators.required],
            contactPersonEmail: [this.companyDetails ? this.companyDetails.contactPerson : '', Validators.required],
            contactPersonPhone: [this.companyDetails ? this.companyDetails.contactPerson : '', Validators.required]
        })
        this.data.dataChange.asObservable()
            .subscribe(res => {
                if (res) {
                    this.companyDetails = res
                }
            })
        this.nextClicked.subscribe(d => this.submittedClicked())
    }


    submittedClicked() {
        console.log("NEXT");
        this.companyDetails["manufacturer"] = this.companyDetailsForm.value
        this.data.setData(this.companyDetails)
    }

}
