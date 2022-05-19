import {Component, ElementRef, OnInit, Renderer2, ViewChild} from '@angular/core';
import {ROUTES} from '../../sidebar/sidebar.component';
import {NavigationEnd, Router} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';
import {Location} from '@angular/common';
import {loadLogout, selectUserInfo} from '../../core/store';
import {Store} from '@ngrx/store';
import {EmailVerificationStatus} from "../../core/store/data/levy/levy.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {LevyService} from "../../core/store/data/levy/levy.service";
import {NotificationService} from "../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import { ReactiveFormsModule } from '@angular/forms';

const misc: any = {
    navbar_menu_visible: 0,
    active_collapse: true,
    disabled_collapse_init: 0,
};

declare var $: any;

@Component({
    selector: 'app-navbar-cmp',
    templateUrl: 'navbar.component.html'
})

export class NavbarComponent implements OnInit {
    private listTitles: any[];
    location: Location;
    mobile_menu_visible: any = 0;
    private nativeElement: Node;
    private toggleButton: any;
    private sidebarVisible: boolean;
    private _router: Subscription;
    roles: string[];
    emailVerificationStatus !: EmailVerificationStatus;
    public emailActivationFormGroup!: FormGroup;
    public activateEmailFormGroup!: FormGroup;
    userId: number;
    email: string;
    loadingTexts: string;
    isShowSendEmailForm=true;
    isShowConfirmEmailForm=true;

    @ViewChild('app-navbar-cmp', {static: false}) button: any;

    constructor(location: Location, private renderer: Renderer2, private element: ElementRef, private store$: Store<any>, public router: Router,private levyService: LevyService,
                private formBuilder: FormBuilder,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService
    ) {
        this.location = location;
        this.nativeElement = element.nativeElement;
        this.sidebarVisible = false;

    }

    minimizeSidebar() {
        const body = document.getElementsByTagName('body')[0];

        if (misc.sidebar_mini_active === true) {
            body.classList.remove('sidebar-mini');
            misc.sidebar_mini_active = false;

        } else {
            setTimeout(function () {
                body.classList.add('sidebar-mini');

                misc.sidebar_mini_active = true;
            }, 300);
        }

        // we simulate the window Resize so the charts will get updated in realtime.
        const simulateWindowResize = setInterval(function () {
            window.dispatchEvent(new Event('resize'));
        }, 180);

        // we stop the simulation of Window Resize after the animations are completed
        setTimeout(function () {
            clearInterval(simulateWindowResize);
        }, 1000);
    }

    hideSidebar() {
        const body = document.getElementsByTagName('body')[0];
        const sidebar = document.getElementsByClassName('sidebar')[0];

        if (misc.hide_sidebar_active === true) {
            setTimeout(function () {
                body.classList.remove('hide-sidebar');
                misc.hide_sidebar_active = false;
            }, 300);
            setTimeout(function () {
                sidebar.classList.remove('animation');
            }, 600);
            sidebar.classList.add('animation');

        } else {
            setTimeout(function() {
                body.classList.add('hide-sidebar');
                // $('.sidebar').addClass('animation');
                misc.hide_sidebar_active = true;
            }, 300);
        }

        // we simulate the window Resize so the charts will get updated in realtime.
        const simulateWindowResize = setInterval(function() {
            window.dispatchEvent(new Event('resize'));
        }, 180);

        // we stop the simulation of Window Resize after the animations are completed
        setTimeout(function() {
            clearInterval(simulateWindowResize);
        }, 1000);
    }

    ngOnInit() {
        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            this.roles = u.roles;
            return this.roles = u.roles;
        });
        this.listTitles = ROUTES.filter(listTitle => listTitle);

        const navbar: HTMLElement = this.element.nativeElement;
        const body = document.getElementsByTagName('body')[0];
        this.toggleButton = navbar.getElementsByClassName('navbar-toggler')[0];
        if (body.classList.contains('sidebar-mini')) {
            misc.sidebar_mini_active = true;
        }
        if (body.classList.contains('hide-sidebar')) {
            misc.hide_sidebar_active = true;
        }
        this._router = this.router.events.filter(event => event instanceof NavigationEnd).subscribe((event: NavigationEnd) => {
            this.sidebarClose();

            const $layer = document.getElementsByClassName('close-layer')[0];
            if ($layer) {
                $layer.remove();
            }
        });
        this.getVerificationStatus();

        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            this.userId = u.id;
            this.email= u.email;
        });
        this.emailActivationFormGroup = this.formBuilder.group({
            userId:[],
            email:[]
        });
        this.activateEmailFormGroup = this.formBuilder.group({
            userId:[],
            email:[],
            verificationToken: []
        });
    }
    showToasterSuccess(title:string,message:string){
        this.notifyService.showSuccess(message, title)

    }
    showToasterError(title:string,message:string){
        this.notifyService.showError(message, title)

    }
    toggleDisplayEmailForm() {
        this.isShowSendEmailForm = !this.isShowSendEmailForm;
        this.isShowConfirmEmailForm=true;

    }

    sendEmailVerificationToken(): void {
        this.loadingTexts = "Generating token ...."
        console.log(this.emailActivationFormGroup.value);
        this.SpinnerService.show();
        this.levyService.sendEmailVerificationToken(this.emailActivationFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Token has been generated and sent to your email Address`);
                this.isShowConfirmEmailForm=!this.isShowConfirmEmailForm;
                this.isShowSendEmailForm=true;
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Generating Token`);
                console.log(error.message);
            }
        );
        this.hideCloseModal();


    }
    @ViewChild('closeModal') private closeModal: ElementRef | undefined;

    public hideCloseModal() {
        this.closeModal?.nativeElement.click();
    }

    confirmEmailAddress(): void {
        this.loadingTexts = "Validating Email Address ...."
        console.log(this.activateEmailFormGroup.value);
        this.SpinnerService.show();
        this.levyService.confirmEmailAddress(this.activateEmailFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Email Address Validated`);
                this.isShowSendEmailForm=true;
                this.isShowConfirmEmailForm=true;
                // this.router.navigateByUrl('/dashboard').then(r => {});
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Validating Email Address`);
                console.log(error.message);
            }
        );
    }
    public getVerificationStatus(): void{
        this.levyService.getVerificationStatus().subscribe(
            (response: EmailVerificationStatus)=> {
                this.emailVerificationStatus = response;
                console.log(this.emailVerificationStatus);
            },
            (error: HttpErrorResponse)=>{
                console.log(error.message)
                //alert(error.message);
            }
        );
    }
    onResize(event) {
        if ($(window).width() > 991) {
            return false;
        }
        return true;
    }
    sidebarOpen() {
        const $toggle = document.getElementsByClassName('navbar-toggler')[0];
        const toggleButton = this.toggleButton;
        const body = document.getElementsByTagName('body')[0];
        setTimeout(function () {
            toggleButton.classList.add('toggled');
        }, 500);
        body.classList.add('nav-open');
        setTimeout(function() {
            $toggle.classList.add('toggled');
        }, 430);

        const $layer = document.createElement('div');
        $layer.setAttribute('class', 'close-layer');


        if (body.querySelectorAll('.main-panel')) {
            document.getElementsByClassName('main-panel')[0].appendChild($layer);
        } else if (body.classList.contains('off-canvas-sidebar')) {
            document.getElementsByClassName('wrapper-full-page')[0].appendChild($layer);
        }

        setTimeout(function() {
            $layer.classList.add('visible');
        }, 100);

        $layer.onclick = function () { // asign a function
            body.classList.remove('nav-open');
            this.mobile_menu_visible = 0;
            this.sidebarVisible = false;

            $layer.classList.remove('visible');
            setTimeout(function () {
                $layer.remove();
                $toggle.classList.remove('toggled');
            }, 400);
        }.bind(this);

        body.classList.add('nav-open');
        this.mobile_menu_visible = 1;
        this.sidebarVisible = true;
    }
    sidebarClose() {
        const $toggle = document.getElementsByClassName('navbar-toggler')[0];
        const body = document.getElementsByTagName('body')[0];
        this.toggleButton.classList.remove('toggled');
        const $layer = document.createElement('div');
        $layer.setAttribute('class', 'close-layer');

        this.sidebarVisible = false;
        body.classList.remove('nav-open');
        // $('html').removeClass('nav-open');
        body.classList.remove('nav-open');
        if ($layer) {
            $layer.remove();
        }

        // setTimeout(function() {
        //     $toggle.classList.remove('toggled');
        // }, 400);

        this.mobile_menu_visible = 0;
    }
    sidebarToggle() {
        if (this.sidebarVisible === false) {
            this.sidebarOpen();
        } else {
            this.sidebarClose();
        }
    }

    getTitle() {
        let titlee = this.location.prepareExternalUrl(this.location.path());
        if (titlee.charAt(0) === '#') {
            titlee = titlee.slice(1);
        }
        for (let i = 0; i < this.listTitles.length; i++) {
            if (this.listTitles[i].type === 'link' && this.listTitles[i].path === titlee) {
                return this.listTitles[i].title;
            } else if (this.listTitles[i].type === 'sub') {
                for (let j = 0; j < this.listTitles[i].children.length; j++) {
                    const subtitle = this.listTitles[i].path + '/' + this.listTitles[i].children[j].path;
                    // console.log(subtitle)
                    // console.log(titlee)
                    if (subtitle === titlee) {
                        return this.listTitles[i].children[j].title;
                    }
                }
            }
        }
        return 'Dashboard';
    }
    getPath() {
        return this.location.prepareExternalUrl(this.location.path());
    }

    onClickLogout() {
        this.store$.dispatch(loadLogout({loginUrl: 'login'}));

    }
}
