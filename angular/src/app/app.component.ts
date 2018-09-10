import {AfterViewInit, Component, ElementRef, OnInit, Renderer, ViewChild} from "@angular/core";
import {Utente} from "./commons/models/utente";
import {AuthenticationService} from "./services/authentication.service";

declare var jQuery: any;

enum MenuOrientation {
    STATIC,
    OVERLAY,
    HORIZONTAL
}

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit {

    layoutCompact: boolean = true;
    layoutMode: MenuOrientation = MenuOrientation.STATIC;
    darkMenu: boolean = false;
    profileMode: string = 'inline';
    rotateMenuButton: boolean;
    topbarMenuActive: boolean;
    overlayMenuActive: boolean;
    staticMenuDesktopInactive: boolean;
    staticMenuMobileActive: boolean;
    layoutContainer: HTMLDivElement;
    layoutMenuScroller: HTMLDivElement;
    modal: HTMLDivElement;
    menuClick: boolean;
    topbarItemClick: boolean;
    activeTopbarItem: any;
    documentClickListener: Function;
    resetMenu: boolean;

    utente: Utente;

    @ViewChild('layoutContainer') layourContainerViewChild: ElementRef;
    @ViewChild('layoutMenuScroller') layoutMenuScrollerViewChild: ElementRef;

    constructor(public renderer: Renderer,
                private authenticationService: AuthenticationService) {
        this.utente = new Utente();
    }

    ngOnInit() {
        this.authenticationService.getUser().subscribe(
            utente => {
                if (utente == null || utente.ruoli == null || utente.ruoli.length == 0) {
                    console.log("should not happen. we have CAS!");
                }
                else {
                    this.utente = utente;
                }
            },
            error => {
            }
        );
    }

    ngAfterViewInit() {
        this.layoutContainer = <HTMLDivElement> this.layourContainerViewChild.nativeElement;
        this.layoutMenuScroller = <HTMLDivElement> this.layoutMenuScrollerViewChild.nativeElement;

        //hides the horizontal submenus or top menu if outside is clicked
        this.documentClickListener = this.renderer.listenGlobal('body', 'click', (event) => {
            if (!this.topbarItemClick) {
                this.activeTopbarItem = null;
                this.topbarMenuActive = false;
            }

            if (!this.menuClick) {
                this.resetMenu = true;
            }

            this.topbarItemClick = false;
            this.menuClick = false;
        });

        setTimeout(() => {
            jQuery(this.layoutMenuScroller).nanoScroller({flash: true});
        }, 10);
    }

    onMenuButtonClick(event) {
        this.rotateMenuButton = !this.rotateMenuButton;
        this.topbarMenuActive = false;

        if (this.layoutMode === MenuOrientation.OVERLAY) {
            this.overlayMenuActive = !this.overlayMenuActive;

            if (this.overlayMenuActive)
                this.enableModal();
            else
                this.disableModal();
        }
        else {
            if (this.isDesktop()) {
                this.staticMenuDesktopInactive = !this.staticMenuDesktopInactive;
            }
            else {
                if (this.staticMenuMobileActive) {
                    this.staticMenuMobileActive = false;
                    this.disableModal();
                }
                else {
                    this.staticMenuMobileActive = true;
                    this.enableModal();
                }
            }
        }

        event.preventDefault();
    }

    onMenuClick($event) {
        this.menuClick = true;
        this.resetMenu = false;

        if (!this.isHorizontal()) {
            setTimeout(() => {
                jQuery(this.layoutMenuScroller).nanoScroller();
            }, 500);
        }
    }

    onTopbarMenuButtonClick(event) {
        this.topbarItemClick = true;
        this.topbarMenuActive = !this.topbarMenuActive;

        if (this.overlayMenuActive || this.staticMenuMobileActive) {
            this.rotateMenuButton = false;
            this.overlayMenuActive = false;
            this.staticMenuMobileActive = false;
            this.disableModal();
        }
    }

    onTopbarItemClick(event, item) {
        this.topbarItemClick = true;

        if (this.activeTopbarItem === item)
            this.activeTopbarItem = null;
        else
            this.activeTopbarItem = item;

        event.preventDefault();
    }

    enableModal() {
        this.modal = document.createElement("div");
        this.modal.className = 'layout-mask';
        this.layoutContainer.appendChild(this.modal);
    }

    disableModal() {
        if (this.modal) {
            this.layoutContainer.removeChild(this.modal);
        }
    }

    isTablet() {
        let width = window.innerWidth;
        return width <= 1024 && width > 640;
    }

    isDesktop() {
        return window.innerWidth > 1024;
    }

    isMobile() {
        return window.innerWidth <= 640;
    }

    isOverlay() {
        return this.layoutMode === MenuOrientation.OVERLAY;
    }

    isHorizontal() {
        return this.layoutMode === MenuOrientation.HORIZONTAL;
    }

    changeToStaticMenu() {
        this.layoutMode = MenuOrientation.STATIC;
    }

    changeToOverlayMenu() {
        this.layoutMode = MenuOrientation.OVERLAY;
    }

    changeToHorizontalMenu() {
        this.layoutMode = MenuOrientation.HORIZONTAL;
    }

    ngOnDestroy() {
        this.disableModal();

        if (this.documentClickListener) {
            this.documentClickListener();
        }

        jQuery(this.layoutMenuScroller).nanoScroller({flash: true});
    }
}
