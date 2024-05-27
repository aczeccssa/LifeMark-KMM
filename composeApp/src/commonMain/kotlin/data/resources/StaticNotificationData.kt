package data.resources

import data.models.MutableNotificationData
import utils.MediaLinkCache

private val STATIC_NOTIFICATION_DATA_LIST: List<List<String>> = listOf(
    listOf("璀璨星辰", "龙嗷嗷地把蛤蟆吃掉了，结果大家都笑喷了。"),
    listOf(
        "BoldJourney",
        "大象奋力跳舞，鲸鱼拿着相机拍了一张萝卜的照片，结果引发了一场洪水，淹没了整个城市，但最后大家都笑了。"
    ),
    listOf("飞舞的花朵", "气球飘飘欲仙，结果被一只大象踩爆了。"),
    listOf(
        "梦想家",
        "大象奋力跳舞，鲸鱼拿着相机拍了一张萝卜的照片，结果引发了一场洪水，淹没了整个城市，但最后大家都笑了。"
    ),
    listOf("幻境行者", "水果大阅兵，苹果挑战梨子的王座。"),
    listOf("RadiantEcho", "蜡烛发现了生命的意义，于是决定去潜水。"),
    listOf("心灵旅者", "乌龟比赛跑步，结果沉睡的美人鱼超越了它。"),
    listOf("笑语如歌", "刺猬上树打麻将，结果吃了别人的牌子。"),
    listOf("DaringAdventurer", "蜘蛛爬上天空，忽然撞上了流星雨。"),
    listOf("神秘探险家", "大象约会了小猫，结果迷路在了冰淇淋森林里。"),
    listOf("ElectricScribe", "草地上长出了彩虹，狗狗跑过去舔了一口。"),
    listOf("霓虹之梦", "钢琴学会了说话，于是唱起了流行歌曲。"),
    listOf(
        "想象者",
        "大象奋力跳舞，鲸鱼拿着相机拍了一张萝卜的照片，结果引发了一场洪水，淹没了整个城市，但最后大家都笑了。"
    ),
    listOf("CelestialExplorer", "咖啡和茶开始争论，结果变成了奶茶。"),
    listOf("温柔笔触", "书包和背包比赛打架，结果变成了背包书包。"),
    listOf("静谧思绪", "天空下起了糖果雨，大家都把伞收了起来。"),
    listOf("LunarWanderer", "鸭子化身成舞者，于是在湖中开起了舞会。"),
    listOf("笑语如歌", "猴子学会拍照，开始了自拍狂欢节。"),
    listOf("沉浸心境", "鲸鱼变成了飞机，带着鸟儿一起环游世界。"),
    listOf(
        "EnigmaticScribe",
        "大象奋力跳舞，鲸鱼拿着相机拍了一张萝卜的照片，结果引发了一场洪水，淹没了整个城市，但最后大家都笑了。"
    ),
    listOf("璀璨星辰", "龙嗷嗷地把蛤蟆吃掉了，结果大家都笑喷了。"),
    listOf("DreamChaser", "啃咬月饼的狐狸不小心咬到了银河系，星星掉了一地卖萌的笑容。"),
    listOf("ColorfulJourney", "世界上最大的蝴蝶和最小的蜜蜂相爱了，结果变成了一对天使。"),
    listOf("WhimsicalExplorer", "草原上长出了彩虹，狼崽子变成了彩色的小羊。"),
    listOf("RadiantWhisper", "月亮和太阳争论时间的长短，最后决定一起旅行到永恒的星空中。"),
    listOf("AdventurousSoul", "狮子和大象一同向希望的远方奔跑，踏碎了一串串的烟花。"),
    listOf("MysteriousDreamer", "洋葱穿越时间回到了过去，看见了可爱的恐龙开着飞船造访地球。"),
    listOf("WhisperingEcho", "海豚跳出海面，变成了夜空中的流星，带来了许愿的机会。"),
    listOf("SmilingAdventurer", "蜜蜂和蝴蝶一起举办了花舞盛宴，彩色的花瓣飘满了整个天空。"),
    listOf("Daydreamer", "松鼠学会了忍者技巧，它们在树上开展了高空战术演练。"),
    listOf("ScribbleOfWonder", "彩虹和流星点亮了夜晚，画出了一副绚丽的星空画卷。"),
    listOf("WhisperingWhimsy", "兔子与鹿在森林中嬉戏，彩虹却被调皮的小鸟戴在了头上。"),
    listOf("JourneyOfIllusion", "夏日的雨滋润了大地，花朵欣喜地开放成了五彩斑斓的小精灵。"),
    listOf("LaughingExplorer", "风车和彩虹竞速，童话世界里充满了欢声笑语。"),
    listOf("WhisperingScribe", "星星变成了音符，而月亮演奏起了美妙的歌曲。"),
    listOf("WonderfulEnigma", "大象化身为画家，用长长的象牙描绘出了宇宙的奥秘。"),
    listOf("MoonlitWander", "蜗牛和龙虾合唱寻找丢失的神奇宝藏，结果竟然找到了彩虹之心。"),
    listOf("WhisperOfImagination", "枯萎的花朵开始飞舞，变成了仙女在花海中翩翩起舞。"),
    listOf("SereneDreamer", "海龟和海豚一起隐身了大海中，结果把夏天变成了海底世界。"),
    listOf("JourneyOfWhispers", "泡泡变成了小精灵，在空中跳跃，传递着美好的祝福。"),
    listOf("EnchantedExplorer", "星星们举办了一场盛大的烟火表演，为夜空增添了绚烂的色彩。"),
    listOf("ScribeOfWhimsy", "银河系的彩虹在七彩的夜空中舞蹈，吸引了地球上的每个人。"),
    listOf("WhisperingAdventure", "山羊和兔子一起举办了滑雪比赛，山坡上的雪花像星星一样闪耀。"),
    listOf("DreamyScribe", "月亮学会了变魔术，把星星一个个放进了自己的口袋。"),
    listOf("WanderingWhispers", "充满活力的海豚和鲸鱼一起开展了海底运动会，水花四溅。"),
    listOf("ScribeOfImagination", "仙鹤在莲花池中歌唱，蝴蝶伴随着音符翩翩起舞。"),
    listOf("EnchantingWhispers", "彩色的云朵和太阳一起跳过天空，把美好的温暖散播给万物。"),
    listOf("DreamyExplorer", "蜜蜂与蜻蜓组成了空中摩天轮，给大地带来了欢声笑语。"),
    listOf("WhisperingWanderings", "小羊驼和小松鼠举办了追赶比赛，结果在预言之树下找到了宝藏。"),
    listOf("MysticalScribe", "天空中的气球变成了彩虹，给人们带来了无尽的希望和梦想。"),
    listOf("ScribeOfWonderment", "流云变成了动人的诗篇，让大家都沉浸在美好的幻境之中。"),
    listOf("WhisperingDreamer", "小兔子跟小鹿在星空下玩耍，点亮了整片黑夜。"),
    listOf("EnchantedJourney", "枯萎的花朵被月亮赐予了魔力，重新开出了绚丽的花朵。"),
    listOf("LuminousWhispers", "夜空中的繁星像精灵一样跳动，为大地带来了灿烂的光芒。"),
    listOf("WhispersOfImagination", "白色棉花糖变成了小精灵，带领大家进入梦幻的甜品王国。"),
    listOf("DreamChaser", "小草变成了翠绿色的毛球，在阳光下欢快地跳跃。"),
)

fun generateNotificationData(): MutableNotificationData {
//    val randomData = STATIC_NOTIFICATION_DATA_LIST.random()
//    val id = data.platform.generateUUID()
//    val image = MediaLinkCache.randomImage()
//    return MutableNotificationData(
//        id = id, title = randomData[0], message = randomData[1], image = image
//    ) { println(id) }
    return MutableNotificationData(
        STATIC_NOTIFICATION_DATA_LIST.random()[0],
        STATIC_NOTIFICATION_DATA_LIST.random()[1],
        MediaLinkCache.randomImage()
    ) { it() }
}