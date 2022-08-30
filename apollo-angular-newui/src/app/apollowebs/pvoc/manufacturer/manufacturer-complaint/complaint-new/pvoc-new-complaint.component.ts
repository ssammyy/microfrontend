import {Component, OnInit} from '@angular/core';
import {Observable, PartialObserver, Subject} from 'rxjs';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {PVOCService} from "../../../../../core/store/data/pvoc/pvoc.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-complaint-new',
    templateUrl: './pvoc-new-complaint.component.html',
    styleUrls: ['./pvoc-new-complaint.component.css'],
})
export class PvocNewComplaintComponent implements OnInit {

    ispause = new Subject();
    time = 59;
    timer!: Observable<number>;
    timerObserver!: PartialObserver<number>;
    step = 1;

    public clicked = false;
    stepOneForm: FormGroup;
    stepTwoForm: FormGroup;
    stepThreeForm: FormGroup;

    uploadedFiles: FileList;
    errors: any = null
    countries: any[] = []
    towns: any[] = []
    selectedBusinessLine = 0;
    selectedBusinessNature = 0;
    selectedRegion = 0;
    selectedCounty = 0;
    selectedTown = 0;
    validationCellphone = '';
    otpSent = false;
    phoneValidated = false;
    // @ts-ignore
    company: Company;
    // @ts-ignore
    user: User;
    submitted = false;


    constructor(
        private pvoc: PVOCService,
        private formBuilder: FormBuilder,
        private router: Router,
    ) {
    }

    ngOnInit(): void {

        this.stepOneForm = this.formBuilder.group({
            registrationNumber: new FormControl('', Validators.required),
            directorIdNumber: new FormControl('', Validators.required),
            firstName: new FormControl('', [Validators.required]),
            lastName: new FormControl('', [Validators.required]),
            emailAddress: new FormControl('', [Validators.required]),
            phoneNumber: new FormControl('', [Validators.required]),
            postalAddress: new FormControl('', [Validators.required]),
        });
        this.stepOneForm.valueChanges.subscribe(
            res=>{
                console.log(res)
            }
        )

        this.stepTwoForm = this.formBuilder.group({
            complaintTitle: new FormControl('', [Validators.required]),
            productName: new FormControl('', [Validators.required]),
            complaintCategory: new FormControl('', [Validators.required]),
            productBrand: new FormControl('', [Validators.required]),
            complaintDescription: new FormControl('', [Validators.required]),
        });

        this.stepTwoForm.valueChanges.subscribe(
            res=>{
                console.log(res)
            }
        )

        this.stepThreeForm = this.formBuilder.group({
            county: new FormControl(),
            town: new FormControl('', [Validators.required]),
            marketCenter: new FormControl('', [Validators.required]),
            buildingName: new FormControl('', [Validators.required]),
        });
        this.stepThreeForm.valueChanges.subscribe(
            res=>{
                console.log(res)
            }
        )
    }

    goToApplications() {
        this.router.navigate(["/company/applications"])
            .then(() => console.log("Back"))
    }

    openComplaintRequest() {

    }

}
