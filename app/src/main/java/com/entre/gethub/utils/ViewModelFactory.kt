package com.entre.gethub.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.entre.gethub.data.preferences.UserPreferences
import com.entre.gethub.data.remote.retrofit.ApiMLService
import com.entre.gethub.data.repositories.AuthRepository
import com.entre.gethub.data.repositories.CariTalentRepository
import com.entre.gethub.data.repositories.CategoryRepository
import com.entre.gethub.data.repositories.CertificationRepository
import com.entre.gethub.data.repositories.GethubRepository
import com.entre.gethub.data.repositories.InformationHubRepository
import com.entre.gethub.data.repositories.LinkRepository
import com.entre.gethub.data.repositories.ProductRepository
import com.entre.gethub.data.repositories.ProfileRepository
import com.entre.gethub.data.repositories.ProjectRepository
import com.entre.gethub.data.repositories.ScanCardRepository
import com.entre.gethub.data.repositories.SponsorRepository
import com.entre.gethub.di.Injection
import com.entre.gethub.ui.akun.AkunViewModel
import com.entre.gethub.ui.auth.LoginViewModel
import com.entre.gethub.ui.auth.RegisterViewModel
import com.entre.gethub.ui.completeprofile.CompleteProfileValidationViewModel
import com.entre.gethub.ui.completeprofile.CompleteProfileViewModel
import com.entre.gethub.ui.gethub.GethubAddPartnerFormViewModel
import com.entre.gethub.ui.gethub.GethubPartnerListViewModel
import com.entre.gethub.ui.gethub.GethubViewModel
import com.entre.gethub.ui.home.HomeViewModel
import com.entre.gethub.ui.home.caritalent.HomeCariTalentViewModel
import com.entre.gethub.ui.home.mygethub.HomeKelolaMyGethubViewModel
import com.entre.gethub.ui.home.mygethub.certification.HomeKelolaMyGethubEditSertifikasiViewModel
import com.entre.gethub.ui.home.mygethub.certification.HomeKelolaMyGethubTambahSertifikasiViewModel
import com.entre.gethub.ui.home.mygethub.link.HomeKelolaMyGethubTambahLinkViewModel
import com.entre.gethub.ui.home.mygethub.product.HomeKelolaMyGethubEditProdukViewModel
import com.entre.gethub.ui.home.mygethub.product.HomeKelolaMyGethubTambahProdukViewModel
import com.entre.gethub.ui.home.mygethub.tentangsaya.HomeKelolaMyGethubEditTentangSayaViewModel
import com.entre.gethub.ui.home.projectbids.HomeCariProjectBidsViewModel
import com.entre.gethub.ui.home.projectbids.HomeDetailProjectBidsFormViewModel
import com.entre.gethub.ui.home.projectbids.HomeDetailProjectBidsViewModel
import com.entre.gethub.ui.home.projectbids.SearchProjectViewModel
import com.entre.gethub.ui.project.ProjectViewModel
import com.entre.gethub.ui.project.bidproject.BidProjectStatusDetailViewModel
import com.entre.gethub.ui.project.bidproject.BidProjectStatusViewModel
import com.entre.gethub.ui.project.postedproject.PostedProjectStatusViewModel
import com.entre.gethub.ui.project.postproject.PostProjectActivity
import com.entre.gethub.ui.project.postproject.PostProjectViewModel
import com.entre.gethub.ui.splash.SplashViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val userPreferences: UserPreferences,
    private val informationHubRepository: InformationHubRepository,
    private val gethubRepository: GethubRepository,
    private val sponsorRepository: SponsorRepository,
    private val productRepository: ProductRepository,
    private val certificationRepository: CertificationRepository,
    private val linkRepository: LinkRepository,
    private val categoryRepository: CategoryRepository,
    private val scanCardRepository: ScanCardRepository,
    private val projectRepository: ProjectRepository,
    private val cariTalentRepository: CariTalentRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            SplashViewModel::class.java -> SplashViewModel(userPreferences) as T
            LoginViewModel::class.java -> LoginViewModel(authRepository, userPreferences) as T
            RegisterViewModel::class.java -> RegisterViewModel(authRepository) as T
            HomeViewModel::class.java -> HomeViewModel(informationHubRepository) as T
            CompleteProfileViewModel::class.java -> CompleteProfileViewModel(profileRepository) as T
            CompleteProfileValidationViewModel::class.java -> CompleteProfileValidationViewModel(
                profileRepository, scanCardRepository
            ) as T

            HomeKelolaMyGethubEditTentangSayaViewModel::class.java -> {
                HomeKelolaMyGethubEditTentangSayaViewModel(profileRepository) as T
            }

            AkunViewModel::class.java -> AkunViewModel(profileRepository, userPreferences) as T
            GethubViewModel::class.java -> GethubViewModel(
                gethubRepository,
                sponsorRepository,
                userPreferences
            ) as T

            HomeCariTalentViewModel::class.java -> HomeCariTalentViewModel(
                cariTalentRepository
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

            GethubAddPartnerFormViewModel::class.java -> GethubAddPartnerFormViewModel(
                gethubRepository
            ) as T

            HomeCariProjectBidsViewModel::class.java -> HomeCariProjectBidsViewModel(
                projectRepository
            ) as T

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

            ProjectViewModel::class.java -> ProjectViewModel(projectRepository) as T

            PostProjectViewModel::class.java -> PostProjectViewModel(
                categoryRepository,
                projectRepository
            ) as T

            PostedProjectStatusViewModel::class.java -> PostedProjectStatusViewModel(
                projectRepository
            ) as T

            SearchProjectViewModel::class.java -> SearchProjectViewModel(projectRepository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        fun getInstance(context: Context): ViewModelFactory =
            ViewModelFactory(
                Injection.provideAuthRepository(context),
                Injection.provideProfileRepository(context),
                Injection.provideUserPreferences(context),
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
            )
    }
}
