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
    labReports: any[] = [];
    qmsReports: any[] = [];
    productList: any[] = [];
    documentation: File[] = [];
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

    // Loading file
    loadFile($event, rtype) {
        if ($event.target.files.length == 0) {
            console.log("No file selected!");
            return
        }
        let file: File = $event.target.files[0];
        // console.log("file selected!",$event.target.files,  file.name);
        this.documentation.push(file)
        switch (rtype) {
            case 'lab':
                this.labReports.push({
                    name: file.name,
                    file
                });
                break
            case 'product':
                this.productList.push({
                    name: file.name,
                    file
                });
                break
            case 'qms':
                this.qmsReports.push({
                    name: file.name,
                    file
                });
                break
            default:
                console.log("Failed")
        }
    }

    deleteFile(i, report) {
        let d: any[] = null
        switch (report) {
            case 'lab':
                d = this.labReports.splice(i, 1);
                break
            case 'qms':
                d = this.qmsReports.splice(i, 1)
                break
            case 'product':
                d = this.productList.splice(i, 1)
                break
            default:
                console.log("Not done: " + report)
        }
        if (d && d.length > 0) {
            this.documentation.filter(f => f.name === d[0].name)
        }

    }

    companyNextClickedEvent() {
        this.companyNextClicked.emit("yes");
    }

    submitExemptionRequest() {
        let data = this.companyDetails
        console.log(data)
        this.pvoc.applyForImportExemption(data, this.documentation ? this.documentation : [])
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
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
