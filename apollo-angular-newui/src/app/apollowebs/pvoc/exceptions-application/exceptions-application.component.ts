import {Component, EventEmitter, OnInit} from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {PVOCService} from "../../../core/store/data/pvoc/pvoc.service";
import {StepperDataService} from "../../../core/services/data/stepper-data.service";
import {Router} from "@angular/router";
import swal from "sweetalert2";

@Component({
    selector: 'app-exceptions-application',
    templateUrl: './exceptions-application.component.html',
    styleUrls: ['./exceptions-application.component.css']
})
export class ExceptionsApplicationComponent implements OnInit {
    companyDetails: any = {};
    goodDetails: any = {}
    companyNextClicked: EventEmitter<any> = new EventEmitter<any>();


    constructor(private router: Router, private fb: FormBuilder, private pvoc: PVOCService, private data: StepperDataService) {
    }

    ngOnInit(): void {
        this.data.dataChange.asObservable()
            .subscribe(
                res => {
                    console.log(res);
                    this.goodDetails = res
                }
            )
    }

    companyNextClickedEvent() {
        this.companyNextClicked.emit("yes");
    }

    submitExemptionRequest() {
        let data = this.companyDetails
        console.log(data)
        this.pvoc.applyForImportExemption(data, this.goodDetails ? this.goodDetails.documentation : [])
            .subscribe(
                res => {
                    if(res.responseCode==='00') {
                        swal.fire({
                            title: res.message,
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        }).then(this.goToHome);
                    } else {
                        swal.fire({
                            title: res.message,
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-danger form-wizard-next-btn ',
                            },
                            icon: 'error'
                        });
                    }
                },
                error => {
                    console.log(error)
                }
            )
    }

    goToHome() {
        this.router.navigateByUrl("/pvoc")
    }

}
