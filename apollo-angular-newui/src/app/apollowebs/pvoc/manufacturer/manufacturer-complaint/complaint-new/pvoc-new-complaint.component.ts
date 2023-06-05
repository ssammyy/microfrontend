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
    partners: any[]
    complaintCategories: any[]
    subcategories: any[] = [];
    submitted = false;
    loading = false


    constructor(
        private pvoc: PVOCService,
        private formBuilder: FormBuilder,
        private router: Router,
    ) {
    }

    ngOnInit(): void {
        this.loadPvoc()

        this.stepOneForm = this.formBuilder.group({
            firstName: new FormControl('', [Validators.required]),
            lastName: new FormControl('', [Validators.required]),
            emailAddress: new FormControl('', [Validators.required]),
            phoneNo: new FormControl('', [Validators.required]),
            address: new FormControl('', [Validators.required]),
        });

        this.stepTwoForm = this.formBuilder.group({
            pvocAgent: new FormControl('', [Validators.required]),
            complaintTitle: new FormControl('', [Validators.required]),
            cocNumber: new FormControl('', []),
            categoryId: new FormControl('', [Validators.required]),
            subCategoryId: new FormControl('0', []),
            rfcNumber: new FormControl('', []),
            complaintDescription: new FormControl('', [Validators.required]),
        });


    }

    selectedComplaintCategories(event: any) {
        this.subcategories = []
        if (this.stepTwoForm.value.categoryId) {
            this.complaintCategories.forEach((cat) => {
                if (cat.categoryId === parseInt(event.target.value)) {
                    this.subcategories = cat.subCategories
                }
            })
        }
        console.log(this.subcategories)
    }

    loadPvoc() {
        // Partners
        this.pvoc.loadPartnerNames()
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
                        this.partners = res.data
                    } else {
                        console.log(res.message)
                    }
                },
                error => {
                    console.error(error)
                }
            )
        // Categories
        this.pvoc.getComplaintCategories()
            .subscribe(res => {
                    if (res.responseCode === "00") {
                        this.complaintCategories = res.data;
                    } else {
                        console.log("Result: " + res.message)
                    }
                },
                error => {
                    console.error(error)
                }
            )
    }

    goToApplications() {
        this.router.navigate(["/manufacturer/complaints"])
            .then(() => console.debug("Back"))
    }

    importFile(event: any) {
        console.log(event)
        if (event.target.files && event.target.files.length == 0) {
            console.log("No file selected!");
            return
        }
        console.log("File selected!");
        let file: File = event.target.files[0];
        //this.uploadedFiles.push(file)

    }

    openComplaintRequest() {

        let d1 = this.stepOneForm.value;
        let d2 = this.stepTwoForm.value;
        let data = {...d1, ...d2};
        console.log(data)
        data.categoryId = parseInt(d2.categoryId)
        data.pvocAgent = parseInt(d2.pvocAgent)
        data.subCategoryId = parseInt(d2.subCategoryId)
        this.loading = true;
        let fileList: File[] = []
        for (let v = 0; v < this.uploadedFiles.length; v++) {
            fileList.push(this.uploadedFiles[v])
        }
        this.pvoc.fileComplaint(data, fileList)
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
                        this.pvoc.showSuccess(res.message, () => this.goToApplications())
                    } else {
                        this.pvoc.showError(res.message, null)
                        this.errors = res.errors
                        this.loading = false;
                    }
                },
                error => {
                    console.log(error)
                    this.loading = false;
                }
            )
    }

}
