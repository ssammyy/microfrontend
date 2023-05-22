import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DataHolder, StdJustification, Stdtsectask} from "../../../core/store/data/std/request_std.model";
import {Department, TechnicalCommittee, UsersEntity} from "../../../core/store/data/std/std.model";
import {StandardDevelopmentService} from "../../../core/store/data/std/standard-development.service";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {selectUserInfo} from "../../../core/store";
import {IDropdownSettings} from "ng-multiselect-dropdown";

declare const $: any;

@Component({
    selector: 'app-tc-management',
    templateUrl: './tc-management.component.html',
    styleUrls: ['./tc-management.component.css']
})
export class TcManagementComponent implements OnInit {

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

    public dropdownSettings: IDropdownSettings = {};
    dropdownList: any[] = [];
    selectedItems: UsersEntity[] = [];


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
        this.getTechnicalCommittee();
        this.getTcSecs()
        this.dropdownSettings = {
            singleSelection: false,
            idField: 'id',
            textField: 'fullName',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            allowSearchFilter: true
        };


    }

    onItemSelect(item: any): void {
        // Add the selected item to the `selectedItems` array
        this.selectedItems.push(item);
    }

    onSelectAll(items: any): void {
        // Add all items to the `selectedItems` array
        this.selectedItems = items;
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

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
    assignTcMembers(formValue: any): void {
        this.loadingText = "Assigning Tc Members ...."
        this.loading = true;
        this.SpinnerService.show();
        const tcId = formValue.tcId;
        const tcMembers = this.selectedItems.map(item => ({tcId, userId: item.id}));

        // Use the `tcMembers` array as needed for further processing or submission
        this.standardDevelopmentService.assignTcMembers(tcMembers).subscribe(
            (response: any) => {
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Members Successfully Assigned`);
                this.getTechnicalCommittee();
                this.hideModelB();
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide();
                this.hideModelB();


            }
        )

    }

    public getTcSecs(): void {
        this.standardDevelopmentService.getTcSec().subscribe(
            (response: UsersEntity[]) => {
                this.tcSecs = response;
                this.dropdownList = response.map(tcSec => ({...tcSec, fullName: this.getFullName(tcSec)}));


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.loading = false;
                this.SpinnerService.hide();
            }
        );
    }

    getFullName(tcSec: UsersEntity): string {
        return `${tcSec.firstName} ${tcSec.lastName}`;
    }


}
