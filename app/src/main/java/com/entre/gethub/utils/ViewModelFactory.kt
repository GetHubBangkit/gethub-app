package com.entre.gethub.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.entre.gethub.data.preferences.UserPreferences
import com.entre.gethub.data.repositories.AnaliticTotalRepository
import com.entre.gethub.data.repositories.AuthRepository
import com.entre.gethub.data.repositories.CardViewersRepository
import com.entre.gethub.data.repositories.CariTalentRepository
import com.entre.gethub.data.repositories.CategoryRepository
import com.entre.gethub.data.repositories.CertificationRepository
import com.entre.gethub.data.repositories.GethubRepository
import com.entre.gethub.data.repositories.GraphDataRepository
import com.entre.gethub.data.repositories.InformationHubRepository
import com.entre.gethub.data.repositories.LinkRepository
import com.entre.gethub.data.repositories.NewPartnerRepository
import com.entre.gethub.data.repositories.PaymentHistoryRepository
import com.entre.gethub.data.repositories.PostCardViewersRepository
import com.entre.gethub.data.repositories.PremiumRepository
import com.entre.gethub.data.repositories.ProductRepository
import com.entre.gethub.data.repositories.ProfileRepository
import com.entre.gethub.data.repositories.ProjectDetectorRepository
import com.entre.gethub.data.repositories.ProjectRepository
import com.entre.gethub.data.repositories.ReysEventRepository
import com.entre.gethub.data.repositories.ScanCardRepository
import com.entre.gethub.data.repositories.ScanKTPRepository
import com.entre.gethub.data.repositories.SponsorRepository
import com.entre.gethub.data.repositories.ThemeHubRepository
import com.entre.gethub.data.repositories.TopTalentRepository
import com.entre.gethub.data.repositories.UserPublicProfileRepository
import com.entre.gethub.data.repositories.VisibilityRepository
import com.entre.gethub.di.Injection
import com.entre.gethub.ui.akun.AkunViewModel
import com.entre.gethub.ui.akun.membership.MembershipViewModel
import com.entre.gethub.ui.akun.paymenthistory.PaymentHistoryViewModel
import com.entre.gethub.ui.analitic.AnaliticViewModel
import com.entre.gethub.ui.auth.LoginViewModel
import com.entre.gethub.ui.auth.RegisterViewModel
import com.entre.gethub.ui.completeprofile.CompleteProfileValidationViewModel
import com.entre.gethub.ui.completeprofile.CompleteProfileViewModel
import com.entre.gethub.ui.detailpartner.DetailPartnerViewModel
import com.entre.gethub.ui.gethub.GethubAddPartnerFormViewModel
import com.entre.gethub.ui.gethub.GethubAddPartnerViewModel
import com.entre.gethub.ui.gethub.GethubPartnerListViewModel
import com.entre.gethub.ui.gethub.GethubViewModel
import com.entre.gethub.ui.home.HomeViewModel
import com.entre.gethub.ui.home.caritalent.HomeCariTalentViewModel
import com.entre.gethub.ui.home.deteksiproject.HomeProjectDetectorViewModel
import com.entre.gethub.ui.home.informationall.HomeInformationAllViewModel
import com.entre.gethub.ui.home.mygethub.HomeKelolaMyGethubGantiDesignViewModel
import com.entre.gethub.ui.home.mygethub.HomeKelolaMyGethubViewModel
import com.entre.gethub.ui.home.mygethub.certification.HomeKelolaMyGethubEditSertifikasiViewModel
import com.entre.gethub.ui.home.mygethub.certification.HomeKelolaMyGethubTambahSertifikasiViewModel
import com.entre.gethub.ui.home.mygethub.link.HomeKelolaMyGethubTambahLinkViewModel
import com.entre.gethub.ui.home.mygethub.product.HomeKelolaMyGethubEditProdukViewModel
import com.entre.gethub.ui.home.mygethub.product.HomeKelolaMyGethubTambahProdukViewModel
import com.entre.gethub.ui.home.mygethub.scanktp.HomeKelolaMyGethubScanKTPMenungguVerifikasiViewModel
import com.entre.gethub.ui.home.mygethub.scanktp.HomeKelolaMyGethubScanKTPViewModel
import com.entre.gethub.ui.home.mygethub.tentangsaya.HomeKelolaMyGethubEditTentangSayaViewModel
import com.entre.gethub.ui.home.projectbids.HomeCariProjectBidsViewModel
import com.entre.gethub.ui.home.projectbids.HomeDetailProjectBidsFormViewModel
import com.entre.gethub.ui.home.projectbids.HomeDetailProjectBidsViewModel
import com.entre.gethub.ui.home.projectbids.HomeMilestoneProjectBidsViewModel
import com.entre.gethub.ui.home.projectbids.SearchProjectViewModel
import com.entre.gethub.ui.project.ProjectViewModel
import com.entre.gethub.ui.project.freelanceracceptedproject.AcceptedBidProjectViewModel
import com.entre.gethub.ui.project.freelancerbidproject.BidProjectStatusDetailViewModel
import com.entre.gethub.ui.project.freelancerbidproject.BidProjectStatusViewModel
import com.entre.gethub.ui.project.chat.ChatViewModel
import com.entre.gethub.ui.project.freelanceracceptedproject.review.OwnerReviewViewModel
import com.entre.gethub.ui.project.freelanceracceptedproject.settlement.FreelancerSettlementViewModel
import com.entre.gethub.ui.project.ownerpostedproject.PostedProjectStatusDetailViewModel
import com.entre.gethub.ui.project.ownerpostedproject.PostedProjectStatusViewModel
import com.entre.gethub.ui.project.ownerpostedproject.payment.OwnerSettlementViewModel
import com.entre.gethub.ui.project.ownerpostedproject.review.FreelancerReviewViewModel
import com.entre.gethub.ui.project.postproject.PostProjectViewModel
import com.entre.gethub.ui.project.postproject.milestone.ProjectMilestoneFormViewModel
import com.entre.gethub.ui.project.postproject.milestone.ProjectMilestoneViewModel
import com.entre.gethub.ui.splash.SplashViewModel
import com.entre.gethub.ui.userpublicprofile.UserPublicProfileViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val userPreferences: UserPreferences,
    private val visibilityRepository: VisibilityRepository,
    private val informationHubRepository: InformationHubRepository,
    private val gethubRepository: GethubRepository,
    private val sponsorRepository: SponsorRepository,
    private val productRepository: ProductRepository,
    private val certificationRepository: CertificationRepository,
    private val linkRepository: LinkRepository,
    private val categoryRepository: CategoryRepository,
    private val scanCardRepository: ScanCardRepository,
    private val projectRepository: ProjectRepository,
    private val cariTalentRepository: CariTalentRepository,
    private val userPublicProfileRepository: UserPublicProfileRepository,
    private val projectDetectorRepository: ProjectDetectorRepository,
    private val themeHubRepository: ThemeHubRepository,
    private val topTalentRepository: TopTalentRepository,
    private val analiticTotalRepository: AnaliticTotalRepository,
    private val newPartnerRepository: NewPartnerRepository,
    private val cardViewersRepository: CardViewersRepository,
    private val postCardViewersRepository: PostCardViewersRepository,
    private val getReysEventRepository: ReysEventRepository,
    private val graphDataRepository: GraphDataRepository,
    private val premiumRepository: PremiumRepository,
    private val paymentHistoryRepository: PaymentHistoryRepository,
    private val scanKTPRepository: ScanKTPRepository

) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            UserPublicProfileViewModel::class.java -> UserPublicProfileViewModel(
                userPublicProfileRepository, postCardViewersRepository
            ) as T


            SplashViewModel::class.java -> SplashViewModel(userPreferences, profileRepository) as T

            SplashViewModel::class.java -> SplashViewModel(userPreferences, profileRepository) as T


            LoginViewModel::class.java -> LoginViewModel(authRepository, userPreferences) as T

            RegisterViewModel::class.java -> RegisterViewModel(authRepository) as T

            HomeViewModel::class.java -> HomeViewModel(
                profileRepository,
                informationHubRepository,
                newPartnerRepository,

                getReysEventRepository,

                projectRepository

            ) as T

            CompleteProfileViewModel::class.java -> CompleteProfileViewModel(profileRepository) as T

            CompleteProfileValidationViewModel::class.java -> CompleteProfileValidationViewModel(
                profileRepository, scanCardRepository
            ) as T

            HomeKelolaMyGethubEditTentangSayaViewModel::class.java ->
                HomeKelolaMyGethubEditTentangSayaViewModel(profileRepository) as T


            AkunViewModel::class.java -> AkunViewModel(
                profileRepository,
                userPreferences,
                visibilityRepository
            ) as T

            GethubViewModel::class.java -> GethubViewModel(
                gethubRepository,
                sponsorRepository,
                userPreferences
            ) as T

            HomeCariTalentViewModel::class.java -> HomeCariTalentViewModel(
                cariTalentRepository, profileRepository
            ) as T

            HomeKelolaMyGethubViewModel::class.java -> HomeKelolaMyGethubViewModel(
                profileRepository,
                productRepository,
                certificationRepository,
                linkRepository,
                categoryRepository
            ) as T

            HomeKelolaMyGethubTambahProdukViewModel::class.java -> HomeKelolaMyGethubTambahProdukViewModel(
                productRepository, categoryRepository
            ) as T

            HomeKelolaMyGethubEditProdukViewModel::class.java -> HomeKelolaMyGethubEditProdukViewModel(
                productRepository, categoryRepository
            ) as T

            HomeKelolaMyGethubEditSertifikasiViewModel::class.java -> HomeKelolaMyGethubEditSertifikasiViewModel(
                certificationRepository, categoryRepository
            ) as T

            HomeKelolaMyGethubTambahSertifikasiViewModel::class.java -> HomeKelolaMyGethubTambahSertifikasiViewModel(
                certificationRepository, categoryRepository
            ) as T

            HomeKelolaMyGethubTambahLinkViewModel::class.java -> HomeKelolaMyGethubTambahLinkViewModel(
                linkRepository
            ) as T

            HomeKelolaMyGethubTambahLinkViewModel::class.java -> HomeKelolaMyGethubTambahLinkViewModel(
                linkRepository
            ) as T

            GethubPartnerListViewModel::class.java -> GethubPartnerListViewModel(
                gethubRepository
            ) as T

            GethubAddPartnerViewModel::class.java -> GethubAddPartnerViewModel(
                gethubRepository, scanCardRepository
            ) as T

            GethubAddPartnerFormViewModel::class.java -> GethubAddPartnerFormViewModel(
                gethubRepository
            ) as T

            HomeCariProjectBidsViewModel::class.java -> HomeCariProjectBidsViewModel(
                projectRepository
            ) as T

            HomeProjectDetectorViewModel::class.java -> {
                HomeProjectDetectorViewModel(projectDetectorRepository) as T
            }

            HomeDetailProjectBidsViewModel::class.java -> HomeDetailProjectBidsViewModel(
                projectRepository
            ) as T

            HomeDetailProjectBidsFormViewModel::class.java -> HomeDetailProjectBidsFormViewModel(
                projectRepository
            ) as T

            BidProjectStatusViewModel::class.java -> BidProjectStatusViewModel(projectRepository) as T

            BidProjectStatusDetailViewModel::class.java -> BidProjectStatusDetailViewModel(
                projectRepository
            ) as T

            ProjectViewModel::class.java -> ProjectViewModel(
                projectRepository,
                topTalentRepository
            ) as T

            PostProjectViewModel::class.java -> PostProjectViewModel(
                categoryRepository,
                projectRepository
            ) as T

            PostedProjectStatusViewModel::class.java -> PostedProjectStatusViewModel(
                projectRepository
            ) as T

            PostedProjectStatusDetailViewModel::class.java -> PostedProjectStatusDetailViewModel(
                projectRepository
            ) as T

            HomeKelolaMyGethubGantiDesignViewModel::class.java -> HomeKelolaMyGethubGantiDesignViewModel(
                profileRepository, themeHubRepository
            ) as T

            AcceptedBidProjectViewModel::class.java -> AcceptedBidProjectViewModel(projectRepository) as T

            SearchProjectViewModel::class.java -> SearchProjectViewModel(projectRepository) as T

            ProjectMilestoneViewModel::class.java -> ProjectMilestoneViewModel(projectRepository) as T

            ProjectMilestoneFormViewModel::class.java -> ProjectMilestoneFormViewModel(
                projectRepository
            ) as T

            HomeMilestoneProjectBidsViewModel::class.java -> HomeMilestoneProjectBidsViewModel(
                projectRepository
            ) as T

            OwnerSettlementViewModel::class.java -> OwnerSettlementViewModel(projectRepository) as T

            FreelancerReviewViewModel::class.java -> FreelancerReviewViewModel(projectRepository) as T

            AnaliticViewModel::class.java -> AnaliticViewModel(
                analiticTotalRepository, cardViewersRepository, graphDataRepository, profileRepository
            ) as T

            ChatViewModel::class.java -> ChatViewModel(profileRepository) as T

            MembershipViewModel::class.java -> MembershipViewModel(premiumRepository) as T

            FreelancerSettlementViewModel::class.java -> FreelancerSettlementViewModel(
                projectRepository
            ) as T

            OwnerReviewViewModel::class.java -> OwnerReviewViewModel(projectRepository) as T

            PaymentHistoryViewModel::class.java -> PaymentHistoryViewModel(paymentHistoryRepository) as T

            HomeInformationAllViewModel::class.java -> HomeInformationAllViewModel(
                informationHubRepository
            ) as T
            DetailPartnerViewModel::class.java -> DetailPartnerViewModel(
                gethubRepository
            ) as T
            HomeKelolaMyGethubScanKTPViewModel::class.java -> HomeKelolaMyGethubScanKTPViewModel(
                scanKTPRepository, profileRepository
            ) as T
            HomeKelolaMyGethubScanKTPMenungguVerifikasiViewModel::class.java -> HomeKelolaMyGethubScanKTPMenungguVerifikasiViewModel(
                scanKTPRepository, profileRepository
            ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        fun getInstance(context: Context): ViewModelFactory =
            ViewModelFactory(
                Injection.provideAuthRepository(context),
                Injection.provideProfileRepository(context),
                Injection.provideUserPreferences(context),
                Injection.provideVisibilityRepository(context),
                Injection.provideInformationHubRepository(context),
                Injection.provideGethubRepository(context),
                Injection.provideSponsorRepository(context),
                Injection.provideProductRepository(context),
                Injection.provideCertificationRepository(context),
                Injection.provideLinkRepository(context),
                Injection.provideCategoryRepository(context),
                Injection.provideScanCardRepository(context),
                Injection.provideProjectRepository(context),
                Injection.provideCariTalentRepository(context),
                Injection.provideUserPublicProfileRepository(context),
                Injection.provideProjectDetectorRepository(context),
                Injection.provideThemeHubRepository(context),
                Injection.provideTopTalentRepository(context),
                Injection.provideAnaliticTotalRepository(context),
                Injection.provideNewPartnerRepository(context),
                Injection.provideCardViewersRepository(context),
                Injection.providePostCardViewersRepository(context),
                Injection.provideReysEventRepository(context),
                Injection.provideGraphDataRepository(context),
                Injection.providePremiumRepository(context),
                Injection.providePaymentHistoryRepository(context),
                Injection.provideScanKTPRepository(context)

            )

    }
}
