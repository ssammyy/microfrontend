import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Department, TechnicalCommittee, UsersEntity} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {selectUserInfo} from "../../../../core/store";
import {Subject} from "rxjs";
import {DataHolder, StdJustification, Stdtsectask} from "../../../../core/store/data/std/request_std.model";
import {DataTableDirective} from "angular-datatables";

declare const $: any;

@Component({
    selector: 'app-createtechnicalcommittee',
    templateUrl: './createtechnicalcommittee.component.html',
    styleUrls: ['./createtechnicalcommittee.component.css']
})
export class CreatetechnicalcommitteeComponent implements OnInit {
    fullname = '';
    p = 1;
    p2 = 1;
    title = 'toaster-not';
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false
    public itemId: number;

    filteredTcSec: number;

    public createTCFormGroup!: FormGroup;

    public tcs !: DataHolder[];
    public departments !: Department[];
    public technicalCommittees !: TechnicalCommittee[];
    public tscsecRequest !: DataHolder | undefined;
    loading = false;
    loadingText: string;
    public tcSecs !: UsersEntity[];


    constructor(
        private formBuilder: FormBuilder,
        private standardDevelopmentService: StandardDevelopmentService,
        private store$: Store<any>,
        private router: Router,
        private notifyService: NotificationService,
        private SpinnerService: NgxSpinnerService,
    ) {
    }

    ngOnInit(): void {
        this.getDepartments();
        this.getTechnicalCommittee();
        this.getTcSecs()


    }


    public getDepartments(): void {
        this.standardDevelopmentService.getDepartmentsb().subscribe(
            (response: Department[]) => {
                this.departments = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            return this.fullname = u.fullName;
        });

        this.createTCFormGroup = this.formBuilder.group({
            title: ['', Validators.required]

        });
    }

    get formCreateTC(): any {
        return this.createTCFormGroup.controls;
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }

    saveTechnicalCommittee(): void {
        this.SpinnerService.show();
        this.standardDevelopmentService.createTechnicalCommittee(this.createTCFormGroup.value).subscribe(
            (response) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Technical Committee Created`);
                this.createTCFormGroup.reset();
                this.getTechnicalCommittee()
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
    }

    showNotification(from: any, align: any) {
        const type = ['', 'info', 'success', 'warning', 'danger', 'rose', 'primary'];

        const color = Math.floor((Math.random() * 6) + 1);

        $.notify({
            icon: 'notifications',
            message: 'ERROR.'
        }, {
            type: type[color],
            timer: 3000,
            placement: {
                from: from,
                align: align
            },
            template: '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0} alert-with-icon" role="alert">' +
                '<button mat-raised-button type="button" aria-hidden="true" class="close" data-notify="dismiss">  <i class="material-icons">close</i></button>' +
                '<i class="material-icons" data-notify="icon">notifications</i> ' +
                '<span data-notify="title"></span> ' +
                '<span data-notify="message">Ensure all required fields and items have been filled</span>' +
                '<div class="progress" data-notify="progressbar">' +
                '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
                '</div>' +
                '<a href="{3}" target="{4}" data-notify="url"></a>' +
                '</div>'
        });
    }

    public getTechnicalCommittee(): void {
        this.loadingText = "Retrieving Please Wait ...."
        this.loading = true;
        this.SpinnerService.show();

        this.standardDevelopmentService.getTechnicalCommittees().subscribe(
            (response: DataHolder[]) => {
                console.log(response);
                this.tcs = response;
                this.loading = false;
                this.SpinnerService.hide();

                if (this.isDtInitialized) {
                    this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                        dtInstance.destroy();
                        this.dtTrigger.next();
                    });
                } else {
                    this.isDtInitialized = true
                    this.dtTrigger.next();
                }
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.loading = false;
                this.SpinnerService.hide();
            }
        )
    }

    public onOpenModal(tcTask: DataHolder, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'edit') {
            this.tscsecRequest = tcTask;
            this.itemId = this.tscsecRequest.id;
            button.setAttribute('data-target', '#uploadSPCJustification');
        }
        if (mode === 'addTcSec') {
            this.tscsecRequest = tcTask;
            this.itemId = this.tscsecRequest.id;
            button.setAttribute('data-target', '#addTcSec');
        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    @ViewChild('closeModalB') private closeModalB: ElementRef | undefined;

    public hideModelB() {
        this.closeModalB?.nativeElement.click();
    }

    uploadJustification(stdJustification: StdJustification): void {

        console.log(stdJustification);

        this.standardDevelopmentService.uploadJustification(stdJustification).subscribe(
            (response: Stdtsectask) => {
                console.log(response);
                // this.getTCSECTasksJustification();
                this.hideModel();
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

    assignTcSec(technical: TechnicalCommittee): void {
        this.SpinnerService.show();
        technical.id = Number(technical.id); // Convert the id value to a number


        if (technical.userId != null) {

            this.standardDevelopmentService.assignTcSec(technical).subscribe(
                (response: any) => {
                     this.SpinnerService.hide();
                    this.showToasterSuccess(response.httpStatus, `Technical Committee Secretary Assigned`);
                    this.getTechnicalCommittee();
                    this.hideModelB();
                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                }
            )
        } else {
            this.showToasterError("Missing Field", `Please Select A User`);

        }
    }

    public getTcSecs()
        :
        void {
        this.standardDevelopmentService.getTcSec().subscribe(
            (response: UsersEntity[]) => {
                this.tcSecs = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }


}
