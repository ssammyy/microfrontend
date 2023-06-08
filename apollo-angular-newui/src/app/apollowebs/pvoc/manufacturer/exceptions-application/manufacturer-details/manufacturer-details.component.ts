import {Component, EventEmitter, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StepperDataService} from "../../../../../core/services/data/stepper-data.service";

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
